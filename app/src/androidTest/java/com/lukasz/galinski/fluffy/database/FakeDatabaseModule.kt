package com.lukasz.galinski.fluffy.database

import android.content.Context
import androidx.room.Room
import com.lukasz.galinski.fluffy.framework.database.AppDatabase
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.framework.database.user.UsersDao
import com.lukasz.galinski.fluffy.framework.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

private const val FAKE_DATABASE_NAME = "testDatabase"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class FakeDatabaseModule {
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
            FAKE_DATABASE_NAME
        ).build()
    }
}