package com.lukasz.galinski.fluffy.framework.di

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.lukasz.galinski.core.domain.DateTimeOperations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    @InstallIn(SingletonComponent::class)
    class ProvideTools {
        @Provides
        fun provideGlide(@ApplicationContext context: Context): RequestManager = Glide.with(context)

        @Provides
        fun provideDateTimeTool() = DateTimeOperations()

        @Provides
        fun provideNetworkData(url: String) =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}