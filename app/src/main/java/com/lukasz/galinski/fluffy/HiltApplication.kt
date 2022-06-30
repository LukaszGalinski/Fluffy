package com.lukasz.galinski.fluffy

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.lukasz.galinski.fluffy.repository.database.AppDatabase
import com.lukasz.galinski.fluffy.repository.database.DatabaseDao
import com.lukasz.galinski.fluffy.repository.database.TransactionsDao
import com.lukasz.galinski.fluffy.repository.database.TransactionsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

private const val TRANSACTIONS_DATABASE_NAME = "TransactionsDatabase"
private const val USERS_DATABASE_NAME = "UsersDatabase"
@HiltAndroidApp
class HiltApplication: Application() {

    @InstallIn(SingletonComponent::class)
    @Module
    class DatabaseModule {
        @Provides
        fun provideChannelDao(appDatabase: AppDatabase): DatabaseDao {
            return appDatabase.usersDao()
        }

        @Provides
        fun provideTransactionsDao(transactionsDatabase: TransactionsDatabase): TransactionsDao {
            return transactionsDatabase.transactionsDao()
        }

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                USERS_DATABASE_NAME
            ).build()
        }

        @Provides
        @Singleton
        fun provideTransactionsDatabase(@ApplicationContext appContext: Context): TransactionsDatabase {
            return Room.databaseBuilder(
                appContext,
                TransactionsDatabase::class.java,
                TRANSACTIONS_DATABASE_NAME
            ).allowMainThreadQueries().build()
        }
    }

    @InstallIn(SingletonComponent::class)
    @Module
    class SharedPreferences{
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context {
            return context
        }
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object DispatcherModule {
        @DefaultDispatcher
        @Provides
        fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

        @IoDispatcher
        @Provides
        fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @MainDispatcher
        @Provides
        fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class DefaultDispatcher

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IoDispatcher

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MainDispatcher
}