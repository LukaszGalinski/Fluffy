package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.di.DispatchersModule
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.account.AccountStates
import com.lukasz.galinski.fluffy.presentation.account.Failure
import com.lukasz.galinski.fluffy.presentation.account.Idle
import com.lukasz.galinski.fluffy.presentation.account.Loading
import com.lukasz.galinski.fluffy.presentation.account.Success
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
    val saveButtonState: LiveData<Boolean> = _saveButtonState
    private val _userAccountState: MutableStateFlow<AccountStates> =
        MutableStateFlow(Idle)
    val userAccountState: StateFlow<AccountStates> = _userAccountState
    private var currentlyLoggedUserId: Long? = null

    fun setSaveButtonState(state: Boolean) {
        _saveButtonState.value = state
    }

    fun saveUserIntoDatabase(user: User) {
        if (_userAccountState.value !is Loading) {
            viewModelScope.launch {
                useCases.addUser(user)
                    .flowOn(ioDispatcher)
                    .onStart { _userAccountState.value = Loading }
                    .catch { _userAccountState.value = Failure(it) }
                    .collect { _userAccountState.value = Success(it) }
            }
        }
    }

    fun loginUser(userLogin: String, userPassword: String) {
        if (_userAccountState.value !is Loading) {
            viewModelScope.launch {
                useCases.loginUser(userLogin, userPassword)
                    .flowOn(ioDispatcher)
                    .onStart { _userAccountState.value = Loading }
                    .catch { _userAccountState.value = Failure(it) }
                    .onCompletion { _userAccountState.value = Idle }
                    .collect {
                        if (it == null) {
                            _userAccountState.value = UserNotFound
                        } else {
                            setLoggedUser(it)
                            _userAccountState.value = Success(it)
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