package com.lukasz.galinski.fluffy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.transaction.TransactionsRepositoryImpl
import com.lukasz.galinski.fluffy.repository.database.user.UsersRepositoryImpl
import com.lukasz.galinski.fluffy.repository.preferences.LoginSharedPreferences
import com.lukasz.galinski.fluffy.view.main.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val DATE_PATTERN = "dd-MM-yyyy"

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val dbRepo: UsersRepositoryImpl,
    private val transactionRepository: TransactionsRepositoryImpl,
    private val sharedPreferences: LoginSharedPreferences,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val dummyUser = UserModel("User", "", "", "")
    private val _loggedUserDetails = MutableStateFlow(dummyUser)
    val loggedUserDetails: StateFlow<UserModel> = _loggedUserDetails
    private val _userMainMenuState: MutableStateFlow<MainMenuStates> = MutableStateFlow(Idle)
    val userMainMenuState: StateFlow<MainMenuStates> = _userMainMenuState
    private val _transactionList = MutableStateFlow(ArrayList<TransactionModel>())
    val transactionList: Flow<ArrayList<TransactionModel>> = _transactionList
    private var userId: Long = 0
    private var currentStartDate = 0L
    private var currentEndDate = 0L

    init {
        userId = sharedPreferences.getLoggedUser()
        getUser(userId)
        getTransactionsList(userId)
    }

    private fun getEndMonthDate(): Long {
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
        val lastDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val endDate = "$lastDayOfMonth-${month + 1}-$year"
        val date = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(endDate)
        currentEndDate = date?.time!!
        return currentEndDate
    }

    private fun getStartMonthDate(): Long {
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val startDate = "01-${month + 1}-$year"
        val date = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(startDate)
        currentStartDate = date?.time!!
        return currentStartDate
    }

    fun getCurrentDate(): Long {
        val calendar = Calendar.getInstance()
        val dateToday = calendar.time
        return dateToday.time
    }

    fun getCurrentMonth(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.MONTH)
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

    private fun getTransactionsList(userId: Long) {
        viewModelScope.launch {
            transactionRepository.getTransactions(userId, getStartMonthDate(), getEndMonthDate())
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
        sharedPreferences.setLoggedUser(0)
    }
}