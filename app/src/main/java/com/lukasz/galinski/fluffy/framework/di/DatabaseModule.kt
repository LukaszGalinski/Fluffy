package com.lukasz.galinski.fluffy.framework.di

import android.content.Context
import androidx.room.Room
import com.lukasz.galinski.fluffy.framework.database.AppDatabase
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.framework.database.user.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "FluffyDatabase"

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): UsersDao {
        return appDatabase.usersDao()
    }

    @Provides
    fun provideTransactionsDao(appDatabase: AppDatabase): TransactionsDao {
        return appDatabase.transactionsDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}