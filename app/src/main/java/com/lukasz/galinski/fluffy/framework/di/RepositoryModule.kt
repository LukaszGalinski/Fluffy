package com.lukasz.galinski.fluffy.framework.di

import com.lukasz.galinski.core.domain.repository.TransactionsRepository
import com.lukasz.galinski.core.domain.repository.UsersRepository
import com.lukasz.galinski.fluffy.framework.database.transaction.RoomTransactionsDataSource
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.framework.database.user.RoomUsersDataSource
import com.lukasz.galinski.fluffy.framework.database.user.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideTransactionRepository(transactionDao: TransactionsDao) =
        TransactionsRepository(RoomTransactionsDataSource(transactionDao))

    @Provides
    fun provideUsersRepository(usersDao: UsersDao) =
        UsersRepository(RoomUsersDataSource(usersDao))
}