package com.lukasz.galinski.fluffy.presentation.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel : ViewModel() {
    private val _onboardingUiEvent = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val onboardingUiEvent: StateFlow<OnboardingUiState> = _onboardingUiEvent

    private var _currentPage = 0

    fun swipeBackIfPossible() {
        if (_currentPage != 0) {
            triggerSwipeBack()
            setSwipeIdleState()
        }
    }

    fun updateCurrentPage(page: Int) {
        _currentPage = page
    }

    private fun triggerSwipeBack() {
        _onboardingUiEvent.value = OnboardingUiState.SwipeBack
    }

    private fun setSwipeIdleState() {
        _onboardingUiEvent.value = OnboardingUiState.Idle
    }
}

sealed class OnboardingUiState {
    object Idle : OnboardingUiState()
    object SwipeBack : OnboardingUiState()
}
