package com.lukasz.galinski.fluffy

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.lukasz.galinski.fluffy.repository.database.AppDatabase
import com.lukasz.galinski.fluffy.repository.database.DatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "UsersDatabase"
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
        @Singleton
        fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}