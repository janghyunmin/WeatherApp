package com.kkjang.domain.usecase

import com.kkjang.domain.repository.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    fun getWeather(
        q: String,
        appId: String
    ) = weatherRepository.getWeather(q, appId)


    fun getLocationWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String,
        appId: String
    ) = weatherRepository.getLocationWeather(lat, lon, lang, units, appId)
}