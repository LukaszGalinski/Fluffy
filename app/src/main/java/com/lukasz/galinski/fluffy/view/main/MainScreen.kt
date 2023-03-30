package com.lukasz.galinski.fluffy.view.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.MainMenuFragmentBinding
import com.lukasz.galinski.fluffy.view.account.LoginHostActivity
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainScreen : Fragment() {
    private var _mainMenuBinding: MainMenuFragmentBinding? = null
    private val mainMenuBinding get() = _mainMenuBinding!!
    private val hostViewModel: MainMenuViewModel by activityViewModels()
    private var transactionAdapter = TransactionsAdapter()
    private var isRotate = false

    companion object {
        private const val MAIN_MENU_TAG = "MainMenu: "
        private const val RECENT_TRANSACTIONS_LIMIT = 20
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
        val currentMonth = hostViewModel.getCurrentMonth()
        createMonthSpinner(currentMonth)
        setupTopBar()
    }

    private fun createFabAnimationButton() {
        val fabAnimation = FabAnimation()
        mainMenuBinding.floatingButton.setOnClickListener {
            setFabAnimation(it, mainMenuBinding.fabOutcome, mainMenuBinding.fabIncome)
        }
        fabAnimation.init(mainMenuBinding.fabIncome)
        fabAnimation.init(mainMenuBinding.fabOutcome)

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

    private fun setupTopBar() {
        mainMenuBinding.materialTopBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.notifications -> {
                    Log.i(MAIN_MENU_TAG, "Notifications")
                    true
                }
                R.id.logout -> {
                    Log.i(MAIN_MENU_TAG, "Logout")
                    hostViewModel.logoutUser()
                    val intent = LoginHostActivity.createIntent(requireContext(), "")
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun createMonthSpinner(currentMonth: Int) {
        val monthArray = resources.getStringArray(R.array.months_of_year)

        val spinnerAdapter: ArrayAdapter<String?> = object :
            ArrayAdapter<String?>(requireContext(), R.layout.spinner_adapter_view, monthArray) {
        }

        val dropdown = mainMenuBinding.monthSpinner
        dropdown.adapter = spinnerAdapter
        dropdown.setSelection(currentMonth)

        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i(MAIN_MENU_TAG, (position + 1).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
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
                        transactionAdapter.transactionsList =
                            getRecentTransactionsList(state.transactionsList)
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

    private fun getRecentTransactionsList(transactionList: ArrayList<Transaction>): ArrayList<Transaction> {
        val recentTransactionsList = ArrayList<Transaction>()
        for (i in transactionList.take(RECENT_TRANSACTIONS_LIMIT).indices) {
            recentTransactionsList.add(transactionList[i])
        }
        return recentTransactionsList
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

    private fun setFabAnimation(
        view: View,
        buttonOutcome: FloatingActionButton,
        buttonIncome: FloatingActionButton
    ) {
        val fabAnimation = FabAnimation()
        isRotate = fabAnimation.rotateFab(view, !isRotate)
        if (isRotate) {
            fabAnimation.showIn(buttonIncome)
            fabAnimation.showIn(buttonOutcome)
        } else {
            fabAnimation.showOut(buttonIncome)
            fabAnimation.showOut(buttonOutcome)
        }
    }

    override fun onDestroy() {
        _mainMenuBinding = null
        super.onDestroy()
    }
}
