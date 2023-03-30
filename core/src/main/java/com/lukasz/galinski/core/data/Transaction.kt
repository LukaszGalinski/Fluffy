package com.lukasz.galinski.core.data

data class Transaction(
    var name: String?,
    var date: Long,
    var category: String?,
    var amount: Double?,
    var description: String?,
    var type: String?,
    var userId: Long,
    var transactionId: Long
)