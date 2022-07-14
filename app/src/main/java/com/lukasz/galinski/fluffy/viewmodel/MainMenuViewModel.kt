package com.lukasz.galinski.fluffy.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.common.DateTools
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.transaction.TransactionsRepositoryImpl
import com.lukasz.galinski.fluffy.repository.database.user.UsersRepositoryImpl
import com.lukasz.galinski.fluffy.repository.preferences.PreferencesData
import com.lukasz.galinski.fluffy.view.main.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val dbRepo: UsersRepositoryImpl,
    private val transactionRepository: TransactionsRepositoryImpl,
    private val sharedPreferencesData: PreferencesData,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var isRotate = false
    private val dateTools = DateTools()
    private val dummyUser = UserModel("User", "", "", "")
    private val _loggedUserDetails = MutableStateFlow(dummyUser)

    //val loggedUserDetails: StateFlow<UserModel> = _loggedUserDetails
    private val _userMainMenuState: MutableStateFlow<MainMenuStates> = MutableStateFlow(Idle)
    val userMainMenuState: StateFlow<MainMenuStates> = _userMainMenuState
    private val _transactionList = MutableStateFlow(ArrayList<TransactionModel>())

    //val transactionList: Flow<ArrayList<TransactionModel>> = _transactionList
    var userID: Long = 0L
    private var currentStartDate = 0L
    private var currentEndDate = 0L

    init {
        viewModelScope.launch {
            sharedPreferencesData.getLoggedUser().collect {
                userID = it
            }
        }
        getUser(userID)
        getTransactionsList(userID)
    }

    fun setFabAnimation(
        view: View,
        buttonOutcome: FloatingActionButton,
        buttonIncome: FloatingActionButton
    ) {
        val fabAnimation = FabAnimation()
        isRotate = fabAnimation.rotateFab(view, !isRotate)
        if (isRotate) {
            fabAnimation.showIn(buttonIncome)
            fabAnimation.showIn(buttonOutcome)
        } else {
            fabAnimation.showOut(buttonIncome)
            fabAnimation.showOut(buttonOutcome)
        }
    }

    private fun getEndMonthDate(): Long {
        currentEndDate = dateTools.getEndMonthDate()
        return currentEndDate
    }

    private fun getStartMonthDate(): Long {
        currentStartDate = dateTools.getStartMonthDate()
        return currentStartDate
    }

    fun getCurrentDate(): Long = dateTools.getCurrentDateInLong()

    fun getCurrentMonth(): Int = dateTools.getCurrentMonthNumber()

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

    private fun getTransactionsList(userId: Long) {
        viewModelScope.launch {
            transactionRepository.getTransactions(
                userId,
                getStartMonthDate(),
                getEndMonthDate()
            )
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
                    _userMainMenuState.emit(Success(it as ArrayList<TransactionModel>))
                    _transactionList.value = it
                }
        }
    }

    fun addNewTransaction(transactionModel: TransactionModel) {
        viewModelScope.launch {
            transactionRepository.addTransaction(transactionModel)
                .onStart {
                    _userMainMenuState.emit(Loading)
                }
                .catch {
                    _userMainMenuState.emit(Failure)
                }.onCompletion {
                    _userMainMenuState.emit(Idle)
                }
                .flowOn(ioDispatcher)
                .collect {
                    if ((currentStartDate < transactionModel.date) && (transactionModel.date < currentEndDate)) {
                        _transactionList.value.add(transactionModel)
                        _userMainMenuState.emit(Success(_transactionList.value))
                    }
                }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            sharedPreferencesData.setLoggedUser(0)
        }
    }
}