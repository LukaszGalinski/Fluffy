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
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.ExpenseAddLayoutBinding
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddTransactionScreen : Fragment() {

    companion object{
        private const val ADD_TRANSACTION_TAG = "AddNewTransaction: "
    }

    private var _expenseScreenBinding: ExpenseAddLayoutBinding? = null
    private val expenseScreenBinding get() = _expenseScreenBinding!!
    private val hostViewModel: MainMenuViewModel by activityViewModels()

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

        val categoryList = resources.getStringArray(R.array.category_array)
        val walletList = resources.getStringArray(R.array.wallet_array)

        val categoryAdapter = SpinnerWithHintAdapter(requireContext(), categoryList)
        val wallerAdapter = SpinnerWithHintAdapter(requireContext(), walletList)

        expenseScreenBinding.spinnerCategory.adapter = categoryAdapter
        expenseScreenBinding.spinnerPaymentMethod.adapter = wallerAdapter
        handleTransactions()
    }

    private fun addNewTransaction(transactionType: String) {
        val newTransaction = TransactionModel(
            name = "Macbook Pro",
            date = hostViewModel.getCurrentDate(),
            category = "Other",
            amount = "659.20",
            description = "5 of 10 debt payment",
            type = transactionType,
            userId = hostViewModel.userID.value
        )
        hostViewModel.addNewTransaction(newTransaction)
    }

    private fun handleTransactions() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.userMainMenuState.collect { state ->
                when (state) {
                    is Success -> {
                        Log.i(ADD_TRANSACTION_TAG, state.toString())
                    }
                    is Failure -> {
                        Log.i(ADD_TRANSACTION_TAG, state.toString())
                    }
                    is Loading -> {
                        Log.i(ADD_TRANSACTION_TAG, state.toString())
                    }
                    is Idle -> {
                        Log.i(ADD_TRANSACTION_TAG, state.toString())
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
