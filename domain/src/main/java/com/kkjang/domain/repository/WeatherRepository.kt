package com.kkjang.domain.repository

import com.kkjang.domain.model.WeatherVo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(
        q: String,
        appId: String,
    ) : Flow<WeatherVo>

    fun getLocationWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String,
        appId: String,
    ) : Flow<WeatherVo>
}