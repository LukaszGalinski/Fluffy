package com.lukasz.galinski.fluffy.view.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lukasz.galinski.fluffy.databinding.ExpenseAddLayoutBinding
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private const val MAIN_MENU_TAG = "MainMenu: "

@AndroidEntryPoint
class AddTransactionScreen : Fragment() {
    private var _expenseScreenBinding: ExpenseAddLayoutBinding? = null
    private val expenseScreenBinding get() = _expenseScreenBinding!!
    private val hostViewModel: MainMenuViewModel by activityViewModels()
    private var transactionAdapter = TransactionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _expenseScreenBinding = ExpenseAddLayoutBinding.inflate(inflater)
        return expenseScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseScreenBinding.lifecycleOwner = viewLifecycleOwner
//        expenseScreenBinding.mainViewModel = hostViewModel
//        expenseScreenBinding.transactions.adapter = transactionAdapter
    }

    private fun addNewTransaction(transactionType: String) {
        val newTransaction = TransactionModel(
            name = "Macbook Pro",
            date = hostViewModel.getCurrentDate(),
            category = "Other",
            amount = "659.20",
            description = "5 of 10 debt payment",
            type = transactionType,
            userId = hostViewModel.userID
        )
        hostViewModel.addNewTransaction(newTransaction)
    }

    private fun handleTransactions() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.userMainMenuState.collect { state ->
                when (state) {
                    is Success -> {
                        Log.i(MAIN_MENU_TAG, state.toString())
                    }
                    is Failure -> {
                        Log.i(MAIN_MENU_TAG, state.toString())
                    }
                    is Loading -> {
                        Log.i(MAIN_MENU_TAG, state.toString())
                    }
                    is Idle -> {
                        Log.i(MAIN_MENU_TAG, state.toString())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _expenseScreenBinding = null
        super.onDestroy()
    }
}
