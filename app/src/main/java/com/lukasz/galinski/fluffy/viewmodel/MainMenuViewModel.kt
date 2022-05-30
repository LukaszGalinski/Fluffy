package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.model.DataModel
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.DatabaseRepositoryImpl
import com.lukasz.galinski.fluffy.repository.database.LoginSharedPreferences
import com.lukasz.galinski.fluffy.repository.remote.NetworkRepository
import com.lukasz.galinski.fluffy.view.main.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val dbRepo: DatabaseRepositoryImpl,
    private val networkRepository: NetworkRepository,
    private val sharedPreferences: LoginSharedPreferences,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val dummyUser = UserModel("User", "", "", "")
    private val _loggedUserDetails = MutableStateFlow(dummyUser)
    val loggedUserDetails: StateFlow<UserModel> = _loggedUserDetails
    private val _userMainMenuState: MutableStateFlow<MainMenuStates> = MutableStateFlow(Idle)
    val userMainMenuState: StateFlow<MainMenuStates> = _userMainMenuState
    private val _transactionList = MutableStateFlow(ArrayList<DataModel>())
    val transactionList: StateFlow<ArrayList<DataModel>> = _transactionList

    init {
        val userId = sharedPreferences.getLoggedUser()
        getUser(userId)
        getNetworkTransactions(userId)
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

    private fun getNetworkTransactions(userId: Long) {
        viewModelScope.launch {
            networkRepository.getTransactions(userId)
                .onStart {
                    _userMainMenuState.emit(Loading)
                }
                .catch {
                    _userMainMenuState.emit(Failure)
                    _transactionList.value = ArrayList()
                }.onCompletion {
                    _userMainMenuState.emit(Idle)
                }
                .flowOn(ioDispatcher)
                .collect {
                    _transactionList.value = it
                    _userMainMenuState.emit(Success(it))
                }
        }
    }

    private fun logoutUser() {
        sharedPreferences.setLoggedUser(0)
    }
}