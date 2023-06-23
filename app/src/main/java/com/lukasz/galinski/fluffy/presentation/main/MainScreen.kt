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
import com.lukasz.galinski.core.domain.TransactionType
import com.lukasz.galinski.fluffy.databinding.MainMenuFragmentBinding
import com.lukasz.galinski.fluffy.presentation.createToast
import com.lukasz.galinski.fluffy.presentation.setGone
import com.lukasz.galinski.fluffy.presentation.setVisible
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
        private const val MAIN_MENU_TAG = "MainMenuFragment"
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
        with(mainMenuBinding) {
            lifecycleOwner = viewLifecycleOwner
            mainViewModel = hostViewModel
            transactions.adapter = transactionAdapter
        }

        createBottomNavigation()
        createFabAnimationButton()
        initFabButtonsView()
        observeUiEvents()
        observeDataStream()
    }

    private fun observeUiEvents() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            hostViewModel.viewEvent.collect {
                when (it) {
                    is MainMenuEvent.ShowFabAnimation -> showInFabButtons()
                    is MainMenuEvent.HideFabAnimation -> showOutFabButtons()
                    is MainMenuEvent.DisplayToast -> {
                        requireContext().createToast(it.message)
                    }

                    is MainMenuEvent.Idle -> Log.i(MAIN_MENU_TAG, it.toString())
                    is MainMenuEvent.IsLoading -> when (it.isLoading) {
                        true -> mainMenuBinding.mainScreenProgressBar.setVisible()
                        false -> mainMenuBinding.mainScreenProgressBar.setGone()
                    }
                }
            }
        }
    }

    private fun observeDataStream() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                hostViewModel.transactionList.collect {
                    configureLineChart(it)
                    transactionAdapter.transactionsList = hostViewModel.getRecentTransactionsList()
                }
            }
        }
    }

    private fun createBottomNavigation() {
        with(mainMenuBinding.bottomNavigationView) {
            background = null
            menu.getItem(2).isEnabled = false
        }
    }


    private fun configureLineChart(data: MutableList<Transaction>) {
        val entryList = ArrayList<Entry>()

        for (i in data.indices) {
            entryList.add(Entry((10 + i).toFloat(), data[i].amount?.toFloat()!!, data[i].date))
        }

        val lineDataSet = LineDataSet(entryList, "Expenses")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        val lineData = LineData(lineDataSet)

        with(mainMenuBinding.chart) {
            description.text = ""
            description.textSize = 0F
            isActivated = true
            setDrawGridBackground(false)
            setGridBackgroundColor(Color.MAGENTA)
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            axisRight.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            legend.isEnabled = false
            performClick()
        }
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

    private fun initFabButtonsView() {
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