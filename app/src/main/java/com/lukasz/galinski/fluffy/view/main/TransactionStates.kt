package com.lukasz.galinski.fluffy.view.main

import com.lukasz.galinski.fluffy.data.model.TransactionModel

sealed class TransactionStates
object Idle : TransactionStates()
object Loading : TransactionStates()
object Failure : TransactionStates()
data class Success(val transactionsList: ArrayList<TransactionModel>) : TransactionStates()

