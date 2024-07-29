package com.kkjang.weather.feature.weather

import com.kkjang.domain.model.WeatherVo

sealed class GetAnotherWeatherState {
    object Init: GetAnotherWeatherState()
    data class Loading(val isLoading: Boolean) : GetAnotherWeatherState()
    data class Success(val response: WeatherVo) : GetAnotherWeatherState()
    data class Fail(val message: String) : GetAnotherWeatherState()
}