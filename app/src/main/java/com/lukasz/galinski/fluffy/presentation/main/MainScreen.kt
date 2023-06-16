package com.lukasz.galinski.fluffy.presentation.main

import android.graphics.Color
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
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.fluffy.databinding.MainMenuFragmentBinding
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainScreen : Fragment() {
    private var _mainMenuBinding: MainMenuFragmentBinding? = null
    private val mainMenuBinding get() = _mainMenuBinding!!
    private val hostViewModel: MainMenuViewModel by activityViewModels()
    private var transactionAdapter = TransactionsAdapter()
    private val fabAnimation = FabAnimation()
    private var isRotate = false

    companion object {
        private const val MAIN_MENU_TAG = "MainMenu: "
    }

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
        createFabAnimationButton()
        resetFabButtonsView()
        observeEvents()
    }

    private fun observeEvents() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.viewEvent.collect {
                when (it) {
                    is MainMenuEvent.ShowFabAnimation -> showInFabButtons()
                    is MainMenuEvent.HideFabAnimation -> showOutFabButtons()
                }
            }
        }
    }

    private fun createBottomNavigation() {
        mainMenuBinding.bottomNavigationView.background = null
        mainMenuBinding.bottomNavigationView.menu.getItem(2).isEnabled = false
    }

    private fun handleTransactions() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.transactionState.collect { state ->
                when (state) {
                    is Success -> {
                        Log.i(MAIN_MENU_TAG, state.toString())
                        configureLineChart(state.transactionsList)
                        transactionAdapter.transactionsList = hostViewModel.getRecentTransactionsList()
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

    private fun configureLineChart(data: ArrayList<Transaction>) {
        mainMenuBinding.chart.description.text = ""
        mainMenuBinding.chart.description.textSize = 0F
        mainMenuBinding.chart.isActivated = true
        mainMenuBinding.chart.setDrawGridBackground(false)
        mainMenuBinding.chart.setGridBackgroundColor(Color.MAGENTA)
        mainMenuBinding.chart.axisLeft.isEnabled = false
        mainMenuBinding.chart.axisRight.isEnabled = false
        mainMenuBinding.chart.axisRight.setDrawGridLines(false)
        mainMenuBinding.chart.axisLeft.setDrawGridLines(false)
        mainMenuBinding.chart.legend.isEnabled = false

        val entryList = ArrayList<Entry>()

        for (i in data.indices) {
            entryList.add(Entry((10 + i).toFloat(), data[i].amount?.toFloat()!!, data[i].date))
        }

        val lineDataSet = LineDataSet(entryList, "Expenses")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        val lineData = LineData(lineDataSet)
        mainMenuBinding.chart.data = lineData
    }

    private fun createFabAnimationButton() {
        mainMenuBinding.floatingButton.setOnClickListener {
            setFabAnimation(it, fabAnimation)
        }

        mainMenuBinding.fabIncome.setOnClickListener {
            mainMenuBinding.floatingButton.performClick()
            createNavigateToNewTransaction(TransactionType.INCOME.label)
        }

        mainMenuBinding.fabOutcome.setOnClickListener {
            mainMenuBinding.floatingButton.performClick()
            createNavigateToNewTransaction(TransactionType.OUTCOME.label)
        }
    }

    private fun createNavigateToNewTransaction(transactionType: String) {
        val action = MainScreenDirections.actionMainScreenToAddTransactionScreen(transactionType)
        findNavController().navigate(action)
    }

    private fun setFabAnimation(view: View, fabAnimation: FabAnimation) {
        isRotate = fabAnimation.rotateFab(view, !isRotate)
        hostViewModel.updateFabAnimation(isRotate)
    }

    private fun showInFabButtons() {
        with(mainMenuBinding) {
            fabAnimation.showIn(fabIncome)
            fabAnimation.showIn(fabOutcome)
        }
    }

    private fun showOutFabButtons() {
        with(mainMenuBinding) {
            fabAnimation.showOut(fabIncome)
            fabAnimation.showOut(fabOutcome)
        }
    }

    private fun resetFabButtonsView() {
        with(mainMenuBinding) {
            fabAnimation.init(fabIncome)
            fabAnimation.init(fabOutcome)
        }
    }

    override fun onDestroy() {
        _mainMenuBinding = null
        super.onDestroy()
    }
}
