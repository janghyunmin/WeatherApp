package com.kkjang.data.repository.remote

import com.kkjang.data.dto.WeatherDto

interface WeatherRemoteDataSource {
    // 날씨 조회
    suspend fun getWeather(q: String, appId: String) : WeatherDto

    // 날씨 조회 ( + 위도 경도 )
    suspend fun getLocationWeather(lat: Double, lon: Double, lang: String, units: String, appId: String) : WeatherDto
}