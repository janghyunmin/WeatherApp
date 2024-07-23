package com.kkjang.data.module.ui

import com.kkjang.data.api.RetrofitService
import com.kkjang.data.module.AppModule
import com.kkjang.data.module.NetModule
import com.kkjang.data.repository.WeatherRepositoryImpl
import com.kkjang.data.repository.local.WeatherLocalDataSource
import com.kkjang.data.repository.local.WeatherLocalDataSourceImpl
import com.kkjang.data.repository.remote.WeatherRemoteDataSource
import com.kkjang.data.repository.remote.WeatherRemoteDataSourceImpl
import com.kkjang.domain.repository.WeatherRepository
import com.kkjang.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetModule::class, AppModule::class])
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): RetrofitService = retrofit.create(RetrofitService::class.java)

    @Singleton
    @Provides
    fun provideWeatherLocalDataSource() : WeatherLocalDataSource = WeatherLocalDataSourceImpl()

    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(retrofitService: RetrofitService): WeatherRemoteDataSource = WeatherRemoteDataSourceImpl(retrofitService = retrofitService)

    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherLocalDataSource: WeatherLocalDataSource,
        weatherRemoteDataSource: WeatherRemoteDataSource
    ) : WeatherRepository = WeatherRepositoryImpl(weatherLocalDataSource, weatherRemoteDataSource)

    @Singleton
    @Provides
    fun provideWeatherUseCase(weatherRepository: WeatherRepository) : GetWeatherUseCase = GetWeatherUseCase(weatherRepository)
}