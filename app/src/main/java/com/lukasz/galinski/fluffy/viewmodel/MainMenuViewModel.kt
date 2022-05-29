package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.DatabaseRepositoryImpl
import com.lukasz.galinski.fluffy.repository.database.LoginSharedPreferences
import com.lukasz.galinski.fluffy.view.main.Failure
import com.lukasz.galinski.fluffy.view.main.Idle
import com.lukasz.galinski.fluffy.view.main.Loading
import com.lukasz.galinski.fluffy.view.main.MainMenuStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val dbRepo: DatabaseRepositoryImpl,
    private val sharedPreferences: LoginSharedPreferences,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val dummyUser = UserModel("User", "", "", "")
    private val _loggedUserDetails = MutableStateFlow(dummyUser)
    val loggedUserDetails: StateFlow<UserModel> = _loggedUserDetails
    private val _userMainMenuState: MutableStateFlow<MainMenuStates> = MutableStateFlow(Idle)
    val userMainMenuState: StateFlow<MainMenuStates> = _userMainMenuState

    init {
        val userId = sharedPreferences.getLoggedUser()
        getUser(userId)
    }

    private fun getUser(userId: Long) =
        viewModelScope.launch {
            dbRepo.getUser(userId)
                .onStart {
                    _userMainMenuState.emit(Loading)
                }
                .catch {
                    _userMainMenuState.emit(Failure)
                }.onCompletion {
                    _userMainMenuState.emit(Idle)
                }
                .flowOn(ioDispatcher)
                .collectLatest {
                    _loggedUserDetails.value = it
                }
        }

    private fun logoutUser(){
        sharedPreferences.setLoggedUser(0)
    }
}