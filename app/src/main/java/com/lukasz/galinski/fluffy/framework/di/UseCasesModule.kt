package com.lukasz.galinski.fluffy.framework.di

import com.lukasz.galinski.core.domain.DateTimeOperations
import com.lukasz.galinski.core.domain.repository.TransactionsRepository
import com.lukasz.galinski.core.domain.repository.UsersRepository
import com.lukasz.galinski.core.domain.usecase.AddTransaction
import com.lukasz.galinski.core.domain.usecase.AddUser
import com.lukasz.galinski.core.domain.usecase.GetTransactionTotalAmount
import com.lukasz.galinski.core.domain.usecase.GetTransactions
import com.lukasz.galinski.core.domain.usecase.GetUser
import com.lukasz.galinski.core.domain.usecase.LoginUser
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionUseCases
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {
    @Provides
    fun getTransactionUseCases(repository: TransactionsRepository, dateTimeOperations: DateTimeOperations) =
        TransactionUseCases(
            AddTransaction(repository, dateTimeOperations),
            GetTransactions(repository),
            GetTransactionTotalAmount()
        )

    @Provides
    fun getUserUseCases(repository: UsersRepository) = UserUseCases(
        AddUser(repository),
        GetUser(repository),
        LoginUser(repository)
    )
}