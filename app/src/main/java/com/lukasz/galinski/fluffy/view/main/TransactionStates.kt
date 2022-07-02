package com.lukasz.galinski.fluffy.view.main

import com.lukasz.galinski.fluffy.model.TransactionModel

sealed class MainMenuStates
object Idle : MainMenuStates()
object Loading : MainMenuStates()
object Failure : MainMenuStates()
data class Success(val transactionsList: ArrayList<TransactionModel>) : MainMenuStates()

