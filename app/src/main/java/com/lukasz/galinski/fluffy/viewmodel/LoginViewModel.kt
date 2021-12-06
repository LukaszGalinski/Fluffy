package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _saveButtonState = MutableLiveData(false)
    val saveButtonState: LiveData<Boolean> = _saveButtonState

    fun setSaveButtonState(state: Boolean) {
        _saveButtonState.value = state
        if (state) println("Hilt validation working")
    }

    fun createDummyMessage() {
        println("Dummy messeage is working with HiLT")
    }
}