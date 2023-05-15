package com.lukasz.galinski.fluffy.framework.di

import com.lukasz.galinski.core.repository.TransactionsRepository
import com.lukasz.galinski.core.repository.UsersRepository
import com.lukasz.galinski.core.usecase.*
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
    fun getTransactionUseCases(repository: TransactionsRepository) = TransactionUseCases(
        AddTransaction(repository),
        GetTransactions(repository)
    )

    @Provides
    fun getUserUseCases(repository: UsersRepository) = UserUseCases(
        AddUser(repository),
        GetUser(repository),
        LoginUser(repository)
    )
}