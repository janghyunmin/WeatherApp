package com.kkjang.data.repository.remote

import com.kkjang.data.api.RetrofitService
import com.kkjang.data.dto.WeatherDto

class WeatherRemoteDataSourceImpl(private val retrofitService: RetrofitService) : WeatherRemoteDataSource {
    override suspend fun getWeather(q: String, appId: String): WeatherDto =
        retrofitService.getWeatherAPI(q = q, appId = appId)

    override suspend fun getLocationWeather(lat: Double, lon: Double, lang: String, units: String, appId: String): WeatherDto =
        retrofitService.getLocationWeatherAPI(lat = lat, lon = lon, lang = lang, units = units, appId = appId)

}