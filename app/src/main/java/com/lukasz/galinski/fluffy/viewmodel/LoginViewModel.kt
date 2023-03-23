package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.data.model.UserModel
import com.lukasz.galinski.fluffy.data.database.user.UsersRepositoryImpl
import com.lukasz.galinski.fluffy.data.preferences.PreferencesData
import com.lukasz.galinski.fluffy.view.account.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dbRepo: UsersRepositoryImpl,
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

    fun saveUserIntoDatabase(userModel: UserModel) = viewModelScope.launch {
        if (_userAccountState.value !is Loading) {
            dbRepo.addNewUser(userModel)
                .onStart {
                    _userAccountState.value = Loading
                }
                .flowOn(ioDispatcher)
                .catch {
                    _userAccountState.value = Failure(it)
                }
                .collect {
                    _userAccountState.value = Success(it)
                }
        }
    }

    fun loginUser(userLogin: String, userPassword: String) {
        viewModelScope.launch {
            if (_userAccountState.value !is Loading) {
                dbRepo.loginUser(userLogin, userPassword)
                    .onStart {
                        _userAccountState.value = Loading
                    }
                    .flowOn(ioDispatcher)
                    .catch {
                        _userAccountState.value = Failure(it)
                    }.onCompletion {
                        _userAccountState.value = Idle
                    }
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