package com.kkjang.weather.feature.weather

import com.kkjang.domain.model.WeatherVo

sealed class GetWeatherState {
    object Init : GetWeatherState()
    data class Loading(val isLoading: Boolean) : GetWeatherState()
    data class Success(val response: WeatherVo) : GetWeatherState()
    data class Fail(val message: String) : GetWeatherState()
}

