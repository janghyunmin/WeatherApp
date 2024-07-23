package com.kkjang.data.repository

import com.kkjang.data.mapper.mapperToWeatherVo
import com.kkjang.data.repository.local.WeatherLocalDataSource
import com.kkjang.data.repository.remote.WeatherRemoteDataSource
import com.kkjang.domain.model.WeatherVo
import com.kkjang.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource): WeatherRepository {
    override fun getWeather(q: String, appId: String): Flow<WeatherVo> = flow {
        emit(weatherRemoteDataSource.getWeather(q, appId).mapperToWeatherVo())
    }

    override fun getLocationWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String,
        appId: String
    ): Flow<WeatherVo> = flow {
        emit(weatherRemoteDataSource.getLocationWeather(lat, lon, lang, units, appId).mapperToWeatherVo())
    }
}