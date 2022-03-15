package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.DatabaseRepositoryImpl
import com.lukasz.galinski.fluffy.view.account.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val dbRepo: DatabaseRepositoryImpl,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loggedUserDetails = MutableStateFlow(UserModel("elko","","","0101"))
    val loggedUserDetails: StateFlow<UserModel> = _loggedUserDetails

    fun getUser(userId: Long){
        println("using get user")
        _loggedUserDetails.value = UserModel("Andrzej", "email", "pass", "0101")
    }


//    nameviewModelScope.launch {
//        if (_userAccountState.value !is Loading) {
//            dbRepo.loginUser(userLogin, userPassword)
//                .onStart {
//                    _userAccountState.value = Loading
//                }
//                .flowOn(ioDispatcher)
//                .catch {
//                    _userAccountState.emit(Failure(it))
//                }.onCompletion {
//                    _userAccountState.emit(Idle)
//                }
//                .collect {
//                    if (it == 0L) {
//                        _userAccountState.emit(UserNotFound(it))
//                    } else {
//                        _userAccountState.emit(Success(it))
//                    }
//                }
//        }
//    }
}