package com.lukasz.galinski.fluffy.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionUseCases
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.di.DispatchersModule
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.main.Failure
import com.lukasz.galinski.fluffy.presentation.main.Idle
import com.lukasz.galinski.fluffy.presentation.main.Loading
import com.lukasz.galinski.fluffy.presentation.main.Success
import com.lukasz.galinski.fluffy.presentation.main.TransactionStates
import com.lukasz.galinski.fluffy.presentation.main.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

private const val ACCOUNT_BALANCE = 8600.00 // Until setup bank connection

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val sharedPreferencesData: PreferencesData,
    @DispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val dateTools = DateTools()
    private val dummyUser = User("User", "", "", "")
    private val _loggedUserDetails = MutableStateFlow(dummyUser)
    private val _transactionState: MutableStateFlow<TransactionStates> = MutableStateFlow(Idle)
    val transactionState: StateFlow<TransactionStates> = _transactionState
    private val _addNewTransactionStatus: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val addNewTransactionStatus: StateFlow<Boolean?> = _addNewTransactionStatus
    private val _transactionList = MutableStateFlow(ArrayList<Transaction>())
    val transactionList: StateFlow<ArrayList<Transaction>> = _transactionList
    private var _userID = MutableStateFlow(0L)
    val userID: StateFlow<Long> = _userID
    private var _transactionIncome = MutableStateFlow(0.0)
    val transactionIncome: StateFlow<Double> = _transactionIncome
    private var _transactionOutcome = MutableStateFlow(0.0)
    val transactionOutcome: StateFlow<Double> = _transactionOutcome
    private var _accountBalance = MutableStateFlow(0.0)
    val accountBalance: StateFlow<Double> = _accountBalance
    private var currentStartDate = 0L
    private var currentEndDate = 0L

    init {
        viewModelScope.launch {
            sharedPreferencesData.getLoggedUser().collect {
                _userID.value = it
                getUser(userID.value)
                getTransactionsList(userID.value)
                _accountBalance.value = getUserBalance()
            }
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
            userUseCases.getUser(userId)
                .onStart { _transactionState.value = Loading }
                .catch { _transactionState.value = Failure }
                .onCompletion { _transactionState.value = Idle }
                .flowOn(ioDispatcher)
                .collect { _loggedUserDetails.value = it }
        }

    private fun getTransactionsList(userId: Long) {
        viewModelScope.launch {
            transactionUseCases.getTransactions(
                userId,
                getStartMonthDate(),
                getEndMonthDate()
            ).onStart { _transactionState.value = Loading }
                .catch {
                    _transactionState.value = Failure
                    _transactionList.value = ArrayList()
                }.onCompletion { _transactionState.value = Idle }
                .flowOn(ioDispatcher)
                .collect { list ->
                    if (list.isNotEmpty()) {
                        _transactionState.value = Success(list as ArrayList<Transaction>)
                        _transactionList.value = list
                    }
                    _transactionIncome.value =
                        round(getIncomeSumOfTransaction(_transactionList.value))
                    _transactionOutcome.value =
                        round(getOutcomeSumOfTransaction(_transactionList.value))
                }
        }
    }

    private fun getOutcomeSumOfTransaction(outcomeList: List<Transaction>): Double {
        return outcomeList.filter { item ->
            item.type == TransactionType.OUTCOME.label
        }.sumOf {
            it.amount!!
        }
    }

    private fun getIncomeSumOfTransaction(incomeList: List<Transaction>): Double {
        return incomeList.filter { item ->
            item.type == TransactionType.INCOME.label
        }.sumOf {
            it.amount!!
        }
    }

    private fun getUserBalance(): Double {
        return ACCOUNT_BALANCE
    }

    fun addNewTransaction(transaction: Transaction) {
        viewModelScope.launch {
            flowOf(transactionUseCases.addTransaction(transaction))
                .catch {
                    _addNewTransactionStatus.value = false
                }
                .onCompletion {
                    _addNewTransactionStatus.value = null
                }
                .flowOn(ioDispatcher)
                .collect {
                    _addNewTransactionStatus.value = true
                    if (newTransactionInTimeRange(transaction.date)) {
                        _transactionList.value.add(0, transaction)
                        _transactionState.value = Success(_transactionList.value)
                        _transactionIncome.value =
                            round(getIncomeSumOfTransaction(_transactionList.value))
                        _transactionOutcome.value =
                            round(getOutcomeSumOfTransaction(_transactionList.value))
                    }
                    _addNewTransactionStatus.value = null
                }
        }
    }

    fun getDoubleFromString(input: String): Double {
        return if (input.isBlank())
            0.toDouble()
        else input.toDouble()
    }

    @VisibleForTesting
    fun newTransactionInTimeRange(date: Long): Boolean {
        return (currentStartDate <= date) && (date <= currentEndDate)
    }

    fun logoutUser() {
        viewModelScope.launch {
            sharedPreferencesData.setLoggedUser(0)
            _userID.value = 0
        }
    }
}

