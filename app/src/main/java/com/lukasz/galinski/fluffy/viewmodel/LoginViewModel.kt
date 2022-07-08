package com.lukasz.galinski.fluffy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.user.UsersRepositoryImpl
import com.lukasz.galinski.fluffy.repository.preferences.PreferencesData
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
                    _userAccountState.emit(Failure(it))
                }.onCompletion {
                    _userAccountState.emit(Idle)
                }
                .collect {
                    _userAccountState.emit(Success(it))
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
                        _userAccountState.emit(Failure(it))
                    }.onCompletion {
                        _userAccountState.emit(Idle)
                    }
                    .collect {
                        if (it == 0L) {
                            _userAccountState.emit(UserNotFound(it))
                        } else {
                            Log.i("ADDING RO SHARED", it.toString())
                            setLoggedUser(it)
                            Log.i("ADDING RO", getLoggedUser().toString())
                            _userAccountState.emit(Success(it))
                        }
                    }
            }
        }
    }

    private fun setLoggedUser(userId: Long) {
        viewModelScope.launch {
            preferencesData.setLoggedUser(userId)
        }
    }

    fun getLoggedUser(): Flow<Long> {
        return preferencesData.getLoggedUser()
    }
}