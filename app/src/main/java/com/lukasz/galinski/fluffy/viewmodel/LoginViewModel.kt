package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.di.DispatchersModule
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.account.Failure
import com.lukasz.galinski.fluffy.presentation.account.Idle
import com.lukasz.galinski.fluffy.presentation.account.Loading
import com.lukasz.galinski.fluffy.presentation.account.LoginStates
import com.lukasz.galinski.fluffy.presentation.account.LoginSuccess
import com.lukasz.galinski.fluffy.presentation.account.RegisterStates
import com.lukasz.galinski.fluffy.presentation.account.RegisterSuccess
import com.lukasz.galinski.fluffy.presentation.account.UserNotFound
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCases: UserUseCases,
    private val preferencesData: PreferencesData,
    @DispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _saveButtonState = MutableLiveData(false)
    private var currentlyLoggedUserId: Long? = null

    val saveButtonState: LiveData<Boolean> = _saveButtonState

    private val _userLoginStates: MutableStateFlow<LoginStates> = MutableStateFlow(Idle)
    val userLoginStates: StateFlow<LoginStates> = _userLoginStates

    private val _userRegisterStates: MutableStateFlow<RegisterStates> = MutableStateFlow(Idle)
    val userRegisterStates: StateFlow<RegisterStates> = _userRegisterStates

    fun setSaveButtonState(state: Boolean) {
        _saveButtonState.value = state
    }

    fun saveUserIntoDatabase(user: User) {
        if (_userLoginStates.value !is Loading) {
            viewModelScope.launch {
                useCases.addUser(user)
                    .flowOn(ioDispatcher)
                    .onStart { _userRegisterStates.value = Loading }
                    .catch { _userRegisterStates.value = Failure(it) }
                    .onCompletion { _userRegisterStates.value = Idle }
                    .collect { _userRegisterStates.value = RegisterSuccess(it) }
            }
        }
    }

    fun loginUser(userLogin: String, userPassword: String) {
        if (_userLoginStates.value !is Loading) {
            viewModelScope.launch {
                useCases.loginUser(userLogin, userPassword)
                    .flowOn(ioDispatcher)
                    .onStart { _userLoginStates.value = Loading }
                    .catch { _userLoginStates.value = Failure(it) }
                    .onCompletion { _userLoginStates.value = Idle }
                    .collect {
                        if (it == null) {
                            _userLoginStates.value = UserNotFound
                        } else {
                            setLoggedUser(it)
                            _userLoginStates.value = LoginSuccess(it)
                        }
                    }
            }
        }
    }

    private fun setLoggedUser(userId: Long) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                preferencesData.setLoggedUser(userId)
                currentlyLoggedUserId = userId
            }
        }
    }

    fun getLoggedUser(): Flow<Long?> =
        if (currentlyLoggedUserId == null) {
            preferencesData.getLoggedUser()
        } else {
            flowOf(currentlyLoggedUserId)
        }
}