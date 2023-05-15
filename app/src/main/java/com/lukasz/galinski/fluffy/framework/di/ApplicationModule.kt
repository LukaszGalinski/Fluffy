package com.lukasz.galinski.fluffy.framework.di

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class ApplicationModule : Application() {

    @InstallIn(SingletonComponent::class)
    @Module
    class SharedPreferences {
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context {
            return context
        }
    }

    @Module
    @InstallIn(FragmentComponent::class)
    class HiltModule {
        @Provides
        fun provideGlide(@ApplicationContext context: Context): RequestManager = Glide.with(context)
    }
}