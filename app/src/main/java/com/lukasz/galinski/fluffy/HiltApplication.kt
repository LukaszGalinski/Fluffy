package com.lukasz.galinski.fluffy

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.lukasz.galinski.fluffy.data.database.AppDatabase
import com.lukasz.galinski.fluffy.data.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.data.database.user.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

private const val DATABASE_NAME = "FluffyDatabase"

@HiltAndroidApp
class HiltApplication : Application() {

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

    @Module
    @InstallIn(FragmentComponent::class)
    class HiltModule {
        @Provides
        fun provideGlide(@ApplicationContext context: Context): RequestManager = Glide.with(context)
    }

    @InstallIn(SingletonComponent::class)
    @Module
    class SharedPreferences {
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