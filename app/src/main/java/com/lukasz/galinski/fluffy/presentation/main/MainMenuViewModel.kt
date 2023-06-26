package com.lukasz.galinski.fluffy.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.core.domain.AddTransactionResult
import com.lukasz.galinski.core.domain.DateTimeOperations
import com.lukasz.galinski.core.domain.SingleTimeEvent
import com.lukasz.galinski.core.domain.TransactionType
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionUseCases
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.di.DispatchersModule
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.account.AppEntryPoint
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

private const val ACCOUNT_BALANCE = 8600.00 // Until setup bank connection

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val sharedPreferencesData: PreferencesData,
    private val dateTimeOperations: DateTimeOperations,
    @DispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private const val RECENT_TRANSACTIONS_LIMIT = 20
    }

    private val _viewEvent = MutableStateFlow<MainMenuEvent>(MainMenuEvent.Idle)
    val viewEvent: StateFlow<MainMenuEvent> = _viewEvent

    private var _transactionIncome = MutableStateFlow(0.0)
    val transactionIncome: StateFlow<Double> = _transactionIncome

    private var _transactionOutcome = MutableStateFlow(0.0)
    val transactionOutcome: StateFlow<Double> = _transactionOutcome

    private var _accountBalance = MutableStateFlow(0.0)
    val accountBalance: StateFlow<Double> = _accountBalance

    private val _loggedUserDetails: MutableStateFlow<User?> = MutableStateFlow(null)

    private val _singleTimeEvent: MutableStateFlow<SingleTimeEvent> =
        MutableStateFlow(SingleTimeEvent.Neutral)
    val singleTimeEvent: StateFlow<SingleTimeEvent> get() = _singleTimeEvent

    private val _transactionList = MutableStateFlow<MutableList<Transaction>>(mutableListOf())
    val transactionList: StateFlow<MutableList<Transaction>> get() = _transactionList

    private var _userID = MutableStateFlow(0L)
    val userID: StateFlow<Long> = _userID

    init {
        viewModelScope.launch {
            sharedPreferencesData.getLoggedUser().collect {
                if (it != null) {
                    _userID.value = it
                    getUserDetails(it)
                    getTransactionsList(it)
                    _accountBalance.value = getUserBalance()
                }
            }
        }
    }

    fun getCurrentDate(): Long = dateTimeOperations.getCurrentDateInLong()

    fun getCurrentMonth(): Int = dateTimeOperations.getCurrentMonthNumber()

    private fun getUserDetails(userId: Long) =
        viewModelScope.launch {
            userUseCases.getUser(userId)
                .flowOn(ioDispatcher)
                .collect { _loggedUserDetails.value = it }
        }

    private fun getTransactionsList(userId: Long) {
        viewModelScope.launch {
            transactionUseCases.getTransactions(
                userId,
                dateTimeOperations.getStartMonthDate(),
                dateTimeOperations.getEndMonthDate()
            )
                .flowOn(ioDispatcher)
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                    showToast(it.message.toString())
                }
                .collect { list ->
                    hideLoading()
                    _transactionList.value = list as MutableList<Transaction>
                    setIncomeValue()
                    setOutcomeValue()
                }
        }
    }

    fun addNewTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionUseCases.addTransaction(transaction)
                .flowOn(ioDispatcher)
                .catch { setSingleTimeOperationResultFailure(it.message.toString()) }
                .onCompletion { setSingleTimeOperationNeutral() }
                .collect {
                    when (it) {
                        AddTransactionResult.SuccessInTimeRange -> {
                            _transactionList.value.add(0, transaction)
                            setSingleTimeOperationResultSuccess()
                        }

                        AddTransactionResult.Success -> {
                            setSingleTimeOperationResultSuccess()
                        }

                        is AddTransactionResult.Error -> {
                            setSingleTimeOperationResultFailure(it.message.toString())
                        }
                    }
                    setIncomeValue()
                    setOutcomeValue()
                }
        }
    }

    private fun setIncomeValue() {
        _transactionIncome.value = transactionUseCases.getTransactionTotalAmount(
            TransactionType.INCOME,
            _transactionList.value
        )
    }

    private fun setOutcomeValue() {
        _transactionOutcome.value = transactionUseCases.getTransactionTotalAmount(
            TransactionType.OUTCOME,
            _transactionList.value
        )
    }

    private fun getUserBalance(): Double {
        return ACCOUNT_BALANCE
    }

    fun getDoubleFromString(input: String): Double {
        return if (input.isBlank())
            0.toDouble()
        else input.toDouble()
    }

    fun logoutUser() {
        viewModelScope.launch {
            sharedPreferencesData.setEntryPoint(AppEntryPoint.LOGIN)
            sharedPreferencesData.clearLoggedUser()
        }
    }

    fun getRecentTransactionsList() = _transactionList.value.take(RECENT_TRANSACTIONS_LIMIT)

    fun updateFabAnimation(status: Boolean) = if (status) {
        _viewEvent.value = MainMenuEvent.ShowFabAnimation
    } else {
        _viewEvent.value = MainMenuEvent.HideFabAnimation
    }

    private fun showToast(message: String) {
        _viewEvent.value = MainMenuEvent.DisplayToast(message)
    }

    private fun setLoading() {
        _viewEvent.value = MainMenuEvent.IsLoading(true)
    }

    private fun hideLoading() {
        _viewEvent.value = MainMenuEvent.IsLoading(false)
    }

    private fun setSingleTimeOperationResultSuccess() {
        _singleTimeEvent.value = SingleTimeEvent.Success
    }

    private fun setSingleTimeOperationResultFailure(message: String) {
        _singleTimeEvent.value = SingleTimeEvent.Failure(message)
    }

    private fun setSingleTimeOperationNeutral() {
        _singleTimeEvent.value = SingleTimeEvent.Neutral
    }
}
