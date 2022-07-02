package com.lukasz.galinski.fluffy.view.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.lukasz.galinski.fluffy.databinding.MainMenuFragmentBinding
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


private const val MAIN_MENU_TAG = "MainMenu: "

@AndroidEntryPoint
class MainScreen : Fragment() {
    private var _mainMenuBinding: MainMenuFragmentBinding? = null
    private val mainMenuBinding get() = _mainMenuBinding!!
    private val hostViewModel: MainMenuViewModel by viewModels()
    private var transactionAdapter = TransactionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mainMenuBinding = MainMenuFragmentBinding.inflate(inflater)
        return mainMenuBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainMenuBinding.lifecycleOwner = viewLifecycleOwner
        mainMenuBinding.mainViewModel = hostViewModel
        mainMenuBinding.transactions.adapter = transactionAdapter

        handleTransactions()
        mainMenuBinding.buttonIncome.setOnClickListener {
            hostViewModel.addNewTransaction(
                TransactionModel(
                    "Macbook Pro",
                    hostViewModel.getCurrentDate(),
                    "Other",
                    "659,20",
                    "5 of 10 debt payment",
                    "outcome",
                    hostViewModel.loggedUserDetails.value.userId
                )
            )
        }
    }

    private fun handleTransactions() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.userMainMenuState.collect { state ->
                when (state) {
                    is Success -> {
                        Log.i(MAIN_MENU_TAG, state.toString())
                        transactionAdapter.transactionsList = state.transactionsList
                        configureLineChart(state.transactionsList)
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

    private fun configureLineChart(data: ArrayList<TransactionModel>) {
        mainMenuBinding.chart.description.text = "My own Data Chart"
        mainMenuBinding.chart.description.textSize = 24F
        mainMenuBinding.chart.isActivated = true

        val entryList = ArrayList<Entry>()

        for (i in data.indices) {
            entryList.add(Entry(i.toFloat(), (2 * i).toFloat(), data[i].amount))
        }

        val lineDataSet = LineDataSet(entryList, "Expenses")
        val lineData = LineData(lineDataSet)

        mainMenuBinding.chart.data = lineData
    }


    override fun onDestroy() {
        _mainMenuBinding = null
        super.onDestroy()
    }
}
