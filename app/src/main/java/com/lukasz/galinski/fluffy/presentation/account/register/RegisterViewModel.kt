package com.lukasz.galinski.fluffy.presentation.account.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.di.DispatchersModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCases: UserUseCases,
    @DispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _registerUiEvent = MutableStateFlow<RegisterUiEvent>(RegisterUiEvent.Idle)
    val registerUiEvent: StateFlow<RegisterUiEvent> = _registerUiEvent

    private val _saveButtonState = MutableStateFlow(false)
    val saveButtonState: StateFlow<Boolean> = _saveButtonState

    fun setSaveButtonState(state: Boolean) {
        _saveButtonState.value = state
    }

    fun saveUserIntoDatabase(user: User) {
        if (_registerUiEvent.value !is RegisterUiEvent.IsLoading) {
            viewModelScope.launch {
                useCases.addUser(user)
                    .flowOn(ioDispatcher)
                    .onStart { setLoading() }
                    .catch {
                        hideLoading()
                        showToast(it)
                    }
                    .onCompletion { setIdle() }
                    .collect {
                        hideLoading()
                        setRegisterSuccess(it)
                    }
            }
        }
    }

    private fun showToast(throwable: Throwable) {
        _registerUiEvent.value = RegisterUiEvent.DisplayToast(throwable)
    }

    private fun setIdle() {
        _registerUiEvent.value = RegisterUiEvent.Idle
    }

    private fun setLoading() {
        _registerUiEvent.value = RegisterUiEvent.IsLoading(true)
    }

    private fun hideLoading() {
        _registerUiEvent.value = RegisterUiEvent.IsLoading(false)
    }

    private fun setRegisterSuccess(userId: Long) {
        _registerUiEvent.value = RegisterUiEvent.RegisterSuccess(userId)
    }
}

sealed class RegisterUiEvent {
    object Idle : RegisterUiEvent()
    data class IsLoading(val isLoading: Boolean) : RegisterUiEvent()
    data class DisplayToast(val exception: Throwable) : RegisterUiEvent()
    data class RegisterSuccess(val userId: Long) : RegisterUiEvent()
}


