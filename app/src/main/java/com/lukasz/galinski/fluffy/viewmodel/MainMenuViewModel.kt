package com.lukasz.galinski.fluffy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukasz.galinski.fluffy.HiltApplication
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.transaction.NetworkRepository
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

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val dbRepo: UsersRepositoryImpl,
    private val networkRepository: NetworkRepository,
    private val sharedPreferences: LoginSharedPreferences,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val dummyUser = UserModel("User", "", "", "")
    private val _loggedUserDetails = MutableStateFlow(dummyUser)
    val loggedUserDetails: StateFlow<UserModel> = _loggedUserDetails
    private val _userMainMenuState: MutableStateFlow<MainMenuStates> = MutableStateFlow(Idle)
    val userMainMenuState: StateFlow<MainMenuStates> = _userMainMenuState
    private val _transactionList = MutableStateFlow(ArrayList<TransactionModel>())
    val transactionList: StateFlow<ArrayList<TransactionModel>> = _transactionList
    private var userId: Long = 0

    init {
        userId = sharedPreferences.getLoggedUser()
        getUser(userId)
        getNetworkTransactions(userId)
    }

    fun getEndDate(): Long {
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
        val lastDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val endDate = "$lastDayOfMonth-${month + 1}-$year"


        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(endDate)

        Log.i("viewModel", endDate)
        Log.i("viewModel", date?.time.toString())

        return date?.time!!
    }

    fun getStartDate(): Long {
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val startDate = "01-${month + 1}-$year"

        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(startDate)

        Log.i("viewModel", startDate)
        Log.i("viewModel", date?.time.toString())
        return date?.time!!
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
            networkRepository.getTransactions(userId, getStartDate(), getEndDate())
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
                    _transactionList.value = it as ArrayList<TransactionModel>
                    _userMainMenuState.emit(Success(it))
                }
        }
    }

    fun addNewRow() {
        val dateToday: Date
        val c: Calendar = Calendar.getInstance()
        dateToday = c.time
        val dateTodayInLong = dateToday.time

        networkRepository.addTransaction(
            TransactionModel(
                "Macbook Pro", dateTodayInLong, "Other", "659,20", "1 of 10 debt payment",
                "outcome", userId
            )
        )
    }

    private fun logoutUser() {
        sharedPreferences.setLoggedUser(0)
    }

}