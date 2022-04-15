package com.eyepax.newsapp.di

import android.content.Context
import com.eyepax.newsapp.AppConstant.Companion.BASE_URL
import com.eyepax.newsapp.db.NewsAppDatabase
import com.eyepax.newsapp.network.NewsApiService
import com.eyepax.newsapp.repository.SharedRepository
import com.eyepax.newsapp.repository.UserPreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NewsApiService::class.java)
    }

    fun provideDashboardRepository(api: NewsApiService): SharedRepository {
        return SharedRepository(api)
    }

    @Provides
    @Singleton
    fun provideNewsAppDatabase(@ApplicationContext appContext: Context): NewsAppDatabase {
        return NewsAppDatabase(appContext)
    }

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext appContext: Context): UserPreferenceRepository {
        return UserPreferenceRepository(appContext)
    }


}