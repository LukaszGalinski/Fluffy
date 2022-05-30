package com.lukasz.galinski.fluffy.view.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lukasz.galinski.fluffy.databinding.MainMenuFragmentBinding
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val MAIN_MENU_TAG = "MainMenu: "

@AndroidEntryPoint
class MainScreen : Fragment() {
    private var _mainMenuBinding: MainMenuFragmentBinding? = null
    private val mainMenuBinding get() = _mainMenuBinding!!
    private val hostViewModel: MainMenuViewModel by viewModels()

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
        handleTransactions()
        mainMenuBinding.transactions.adapter = TransactionsAdapter(ArrayList())
    }

    private fun handleTransactions() = lifecycleScope.launchWhenStarted {
        hostViewModel.userMainMenuState.collect { state ->
            when (state) {
                is Success -> {
                    Log.i(MAIN_MENU_TAG, state.toString())
                    mainMenuBinding.transactions.adapter =
                        TransactionsAdapter(state.transactionsList)
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