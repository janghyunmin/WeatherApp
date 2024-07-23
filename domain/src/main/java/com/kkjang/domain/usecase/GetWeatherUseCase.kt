package com.kkjang.domain.usecase

import com.kkjang.domain.repository.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    operator fun invoke(
        q: String,
        appId: String
    ) = weatherRepository.getWeather(q, appId)
}