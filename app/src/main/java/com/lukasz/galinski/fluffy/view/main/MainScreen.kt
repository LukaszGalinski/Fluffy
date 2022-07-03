package com.lukasz.galinski.fluffy.view.main

import android.graphics.Color
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
    private var isRotate = false

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

        createBottomNavigation()
        handleTransactions()
        mainMenuBinding.buttonIncome.setOnClickListener {
            addDummyTransaction()
        }
    }

    private fun addDummyTransaction() = hostViewModel.addNewTransaction(
        TransactionModel(
            "Macbook Pro",
            hostViewModel.getCurrentDate(),
            "Other",
            "659.20",
            "5 of 10 debt payment",
            "outcome",
            hostViewModel.loggedUserDetails.value.userId
        )
    )

    private fun createBottomNavigation() {
        mainMenuBinding.bottomNavigationView.background = null
        mainMenuBinding.bottomNavigationView.menu.getItem(2).isEnabled = false

        mainMenuBinding.floatingButton.setOnClickListener {

            isRotate = AnimatedFab().rotateFab(it, !isRotate)
            if (isRotate) {
                AnimatedFab().showIn(mainMenuBinding.fabIncome)
                AnimatedFab().showIn(mainMenuBinding.fabOutcome)
            } else {
                AnimatedFab().showOut(mainMenuBinding.fabIncome)
                AnimatedFab().showOut(mainMenuBinding.fabOutcome)
            }
        }
        AnimatedFab().init(mainMenuBinding.fabIncome)
        AnimatedFab().init(mainMenuBinding.fabOutcome)
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
        mainMenuBinding.chart.description.text = ""
        mainMenuBinding.chart.description.textSize = 0F
        mainMenuBinding.chart.isActivated = true
        mainMenuBinding.chart.setDrawGridBackground(false)
        mainMenuBinding.chart.setGridBackgroundColor(Color.MAGENTA)
        mainMenuBinding.chart.axisLeft.isEnabled = false
        mainMenuBinding.chart.axisRight.isEnabled = false
        mainMenuBinding.chart.axisRight.setDrawGridLines(false)
        mainMenuBinding.chart.axisLeft.setDrawGridLines(false)

        val entryList = ArrayList<Entry>()

        for (i in data.indices) {
            entryList.add(Entry((10+i).toFloat(), data[i].amount?.toFloat()!!, data[i].date))
        }

        val lineDataSet = LineDataSet(entryList, "Expenses")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        val lineData = LineData(lineDataSet)

        mainMenuBinding.chart.data = lineData
    }


    override fun onDestroy() {
        _mainMenuBinding = null
        super.onDestroy()
    }
}
