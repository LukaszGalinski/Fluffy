package com.lukasz.galinski.fluffy.presentation.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.core.domain.BaseResult
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.di.DispatchersModule
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.account.AppEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    private val _loginUiEvent = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiEvent: StateFlow<LoginUiState> = _loginUiEvent

    fun loginUser(userLogin: String, userPassword: String) {
        if (_loginUiEvent.value !is LoginUiState.IsLoading) {
            viewModelScope.launch {
                useCases.loginUser(userLogin, userPassword)
                    .flowOn(ioDispatcher)
                    .onStart { setLoading() }
                    .catch {
                        hideLoading()
                        showToast(it)
                    }
                    .onCompletion { setIdle() }
                    .collect {
                        hideLoading()
                        when (it) {
                            is BaseResult.Success -> {
                                setLoggedUser(it.userId)
                                setLoginSuccess()
                            }

                            is BaseResult.Error -> setError()
                        }
                    }
            }
        }
    }

    fun setEntryPoint(entryPoint: AppEntryPoint) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                preferencesData.setEntryPoint(entryPoint)
            }
        }
    }

    fun getEntryPoint(): Flow<AppEntryPoint> =
        preferencesData.getEntryPoint().map {
            when (it) {
                null -> AppEntryPoint.ONBOARDING
                else -> AppEntryPoint.valueOf(it)
            }
        }

    private fun setLoggedUser(userId: Long) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                preferencesData.setLoggedUser(userId)
            }
        }
    }

    /**
     * Run on MainThread as it is entry point
     */
    fun getLoggedUser(): Flow<Long?> = preferencesData.getLoggedUser()

    private fun showToast(throwable: Throwable) {
        _loginUiEvent.value = LoginUiState.DisplayToast(throwable)
    }

    private fun setIdle() {
        _loginUiEvent.value = LoginUiState.Idle
    }

    private fun setLoading() {
        _loginUiEvent.value = LoginUiState.IsLoading(true)
    }

    private fun hideLoading() {
        _loginUiEvent.value = LoginUiState.IsLoading(false)
    }

    private fun setLoginSuccess() {
        setEntryPoint(AppEntryPoint.MAIN_MENU)
        _loginUiEvent.value = LoginUiState.LoginSuccess
    }

    private fun setError() {
        _loginUiEvent.value = LoginUiState.UserNotFound
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object UserNotFound : LoginUiState()
    object LoginSuccess : LoginUiState()
    data class IsLoading(val isLoading: Boolean) : LoginUiState()
    data class DisplayToast(val exception: Throwable) : LoginUiState()
}
