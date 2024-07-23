package com.kkjang.data.module

import android.app.Application
import com.google.gson.GsonBuilder
import com.kkjang.data.api.RetrofitService
import com.kkjang.data.module.interceptor.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            client(okHttpClient)
            baseUrl(RetrofitService.BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
        application: Application
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            pingInterval(30, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(loggingInterceptor)
            addInterceptor { chain ->
                val originalRequest = chain.request()
                val response = chain.proceed(originalRequest)
                response
            }
        }.build()
    }


    @Singleton
    @Provides
    fun provideAuthorizationInterceptor(): Interceptor = AuthorizationInterceptor()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        }
        return httpLoggingInterceptor
    }
}