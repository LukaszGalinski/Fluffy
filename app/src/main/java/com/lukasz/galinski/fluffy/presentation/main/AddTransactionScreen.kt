package com.lukasz.galinski.fluffy.presentation.main

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
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.domain.SingleTimeEvent
import com.lukasz.galinski.core.domain.TransactionCategories
import com.lukasz.galinski.core.domain.TransactionType
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.TransactionAddLayoutBinding
import com.lukasz.galinski.fluffy.presentation.common.createToast
import com.lukasz.galinski.fluffy.presentation.common.handleBackPress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddTransactionScreen : Fragment() {

    companion object {
        private const val ADD_TRANSACTION_TAG = "AddNewTransaction"
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
        observeAddNewTransactionResult()
        handleBackPress { findNavController().popBackStack()}

        transactionScreenBinding.addNewTransaction.setOnClickListener {
            addNewTransaction(getTransactionDetailsFromFields(arguments.transactionTypeArgument))
        }

        transactionScreenBinding.iconBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun hideAppTopBar() {
        (activity as MainMenuActivity).hideAppBar()
    }

    private fun showAppTopBar() {
        (activity as MainMenuActivity).showAppBar()
    }

    private fun getTransactionDetailsFromFields(transactionType: String): Transaction =
        Transaction(
            name = transactionScreenBinding.transactionName.text.toString(),
            date = hostViewModel.getCurrentDate(),
            category = transactionScreenBinding.spinnerCategory.selectedItem.toString(),
            amount = hostViewModel.getDoubleFromString(
                transactionScreenBinding.etAmount.text.toString()
            ),
            description = transactionScreenBinding.etDescription.text.toString(),
            type = transactionType,
            userId = hostViewModel.userID.value,
            transactionId = 0
        )

    private fun buildCategoryList() {
        val categoryAdapter =
            SpinnerWithHintAdapter(requireContext(), TransactionCategories.values())
        transactionScreenBinding.spinnerCategory.adapter = categoryAdapter
    }

    private fun buildWalletList() {
        val paymentMethodAdapter =
            SpinnerWithHintAdapter(requireContext(), TransactionPaymentMethod.values())
        transactionScreenBinding.spinnerPaymentMethod.adapter = paymentMethodAdapter
    }

    private fun createTransactionTypeView(transactionType: String) {
        when (transactionType) {
            TransactionType.INCOME.label -> {
                transactionScreenBinding.onboardingTitle.text = resources.getString(
                    R.string.add_new_income_label
                )
                transactionScreenBinding.transactionAddMain.setBackgroundColor(
                    ResourcesCompat.getColor(resources, R.color.dark_green, null)
                )
            }

            TransactionType.OUTCOME.label -> {
                transactionScreenBinding.onboardingTitle.text = resources.getString(
                    R.string.add_new_expense_label
                )
                transactionScreenBinding.transactionAddMain.setBackgroundColor(
                    ResourcesCompat.getColor(resources, R.color.dark_red, null)
                )
            }
        }
    }

    private fun addNewTransaction(transaction: Transaction) {
        hostViewModel.addNewTransaction(transaction)
    }

    private fun observeAddNewTransactionResult() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.singleTimeEvent.collect { status ->
                when (status) {
                    SingleTimeEvent.Success -> {
                        Log.i(ADD_TRANSACTION_TAG, status.toString())
                        showToast(R.string.transaction_add_success)
                        findNavController().popBackStack()
                    }

                    is SingleTimeEvent.Failure -> {
                        Log.i(ADD_TRANSACTION_TAG, status.message)
                        showToast(R.string.transaction_add_failure)
                    }

                    SingleTimeEvent.Neutral ->
                        Log.i(ADD_TRANSACTION_TAG, status.toString())

                }
            }
        }
    }

    private fun showToast(message: Int) {
        requireContext().createToast(resources.getString(message))
    }

    override fun onDestroy() {
        _transactionScreenBinding = null
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        showAppTopBar()
    }

    override fun onResume() {
        super.onResume()
        hideAppTopBar()
    }
}
