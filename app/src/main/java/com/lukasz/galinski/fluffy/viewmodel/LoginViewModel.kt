package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.account.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCases: UserUseCases,
    private val preferencesData: PreferencesData,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _saveButtonState = MutableLiveData(false)
    val saveButtonState: LiveData<Boolean> = _saveButtonState
    private val _userAccountState: MutableStateFlow<AccountStates> =
        MutableStateFlow(Idle)
    val userAccountState: StateFlow<AccountStates> = _userAccountState
    private var currentlyLoggedUserId: Long = 0L

    fun setSaveButtonState(state: Boolean) {
        _saveButtonState.value = state
    }

    fun saveUserIntoDatabase(user: User) {
        if (_userAccountState.value !is Loading) {
            viewModelScope.launch {
                useCases.addUser(user)
                    .onStart { _userAccountState.value = Loading }
                    .flowOn(ioDispatcher)
                    .catch { _userAccountState.value = Failure(it) }
                    .collect { _userAccountState.value = Success(it) }
            }
        }
    }

    fun loginUser(userLogin: String, userPassword: String) {
        if (_userAccountState.value !is Loading) {
            viewModelScope.launch {
                useCases.loginUser(userLogin, userPassword)
                    .onStart { _userAccountState.value = Loading }
                    .catch { _userAccountState.value = Failure(it) }
                    .onCompletion { _userAccountState.value = Idle }
                    .flowOn(ioDispatcher)
                    .collect {
                        if (it == 0L) {
                            _userAccountState.value = UserNotFound(it)
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
            preferencesData.setLoggedUser(userId)
            currentlyLoggedUserId = userId
        }
    }

    fun getLoggedUser(): Flow<Long> {
        return if (currentlyLoggedUserId == 0L) {
            preferencesData.getLoggedUser()
        } else {
            flowOf(currentlyLoggedUserId)
        }
    }
}