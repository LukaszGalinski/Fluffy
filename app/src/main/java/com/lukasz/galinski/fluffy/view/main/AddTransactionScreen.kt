package com.lukasz.galinski.fluffy.view.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.createToast
import com.lukasz.galinski.fluffy.data.model.TransactionModel
import com.lukasz.galinski.fluffy.databinding.TransactionAddLayoutBinding
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddTransactionScreen : Fragment() {

    companion object {
        private const val ADD_TRANSACTION_TAG = "AddNewTransaction: "
    }

    private var _transactionScreenBinding: TransactionAddLayoutBinding? = null
    private val transactionScreenBinding get() = _transactionScreenBinding!!
    private val hostViewModel: MainMenuViewModel by activityViewModels()
    private val arguments: AddTransactionScreenArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _transactionScreenBinding = TransactionAddLayoutBinding.inflate(inflater)
        createTransactionTypeView(arguments.transactionTypeArgument)
        return transactionScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionScreenBinding.lifecycleOwner = viewLifecycleOwner
        buildCategoryList()
        buildWalletList()
        handleTransactions()

        transactionScreenBinding.addNewTransaction.setOnClickListener {
            addNewTransaction(arguments.transactionTypeArgument)
        }

        transactionScreenBinding.iconBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_addTransactionScreen_to_mainScreen)
        }
    }

    private fun buildCategoryList() {
        val categoryList = resources.getStringArray(R.array.category_array)
        val categoryAdapter = SpinnerWithHintAdapter(requireContext(), categoryList)
        transactionScreenBinding.spinnerCategory.adapter = categoryAdapter
    }

    private fun buildWalletList() {
        val walletList = resources.getStringArray(R.array.wallet_array)
        val wallerAdapter = SpinnerWithHintAdapter(requireContext(), walletList)
        transactionScreenBinding.spinnerPaymentMethod.adapter = wallerAdapter
    }

    private fun createTransactionTypeView(transactionType: String) {
        if (transactionType == TransactionType.INCOME.label) {
            transactionScreenBinding.onboardingTitle.text = resources.getString(
                R.string.add_new_income_label
            )
            transactionScreenBinding.transactionAddMain.setBackgroundColor(
                ResourcesCompat.getColor(resources, R.color.dark_green, null)
            )
        } else {
            transactionScreenBinding.onboardingTitle.text = resources.getString(
                R.string.add_new_expense_label
            )
            transactionScreenBinding.transactionAddMain.setBackgroundColor(
                ResourcesCompat.getColor(resources, R.color.dark_red, null)
            )
        }
    }

    private fun addNewTransaction(transactionType: String) {
        val newTransaction = TransactionModel(
            name = transactionScreenBinding.transactionName.text.toString(),
            date = hostViewModel.getCurrentDate(),
            category = transactionScreenBinding.spinnerCategory.selectedItem.toString(),
            amount = hostViewModel.getDoubleFromString(
                transactionScreenBinding.etAmount.text.toString()
            ),
            description = transactionScreenBinding.etDescription.text.toString(),
            type = transactionType,
            userId = hostViewModel.userID.value
        )
        hostViewModel.addNewTransaction(newTransaction)
    }

    private fun handleTransactions() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.addNewTransactionStatus.collect { addTransactionStatus ->
                when (addTransactionStatus) {
                    true -> {
                        Log.i(ADD_TRANSACTION_TAG, "Success")
                        findNavController().popBackStack()
                    }
                    false -> {
                        Log.i(ADD_TRANSACTION_TAG, "Failure")
                        context?.createToast(resources.getString(R.string.transaction_add_failure))
                    }
                    else -> {
                        Log.i(ADD_TRANSACTION_TAG, "IDLE")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _transactionScreenBinding = null
        super.onDestroy()
    }
}
