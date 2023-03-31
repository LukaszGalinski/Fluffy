package com.lukasz.galinski.fluffy.presentation.main

import com.lukasz.galinski.core.data.Transaction

sealed class TransactionStates
object Idle : TransactionStates()
object Loading : TransactionStates()
object Failure : TransactionStates()
data class Success(val transactionsList: ArrayList<Transaction>) : TransactionStates()

