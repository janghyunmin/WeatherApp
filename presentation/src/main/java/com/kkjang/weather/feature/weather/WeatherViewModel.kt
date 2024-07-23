package com.kkjang.weather.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkjang.data.module.provider.ResourcesProvider
import com.kkjang.data.util.default
import com.kkjang.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val resourcesProvider: ResourcesProvider,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<GetWeatherState>(GetWeatherState.Init)
    val weatherState = _weatherState.asStateFlow()

    fun getWeather(q: String, appId: String) {
        viewModelScope.launch {
            getWeatherUseCase.getWeather(q = q, appId = appId)
                .onStart {
                    _weatherState.value = GetWeatherState.Loading(true)
                }
                .catch { exception ->
                    _weatherState.value = GetWeatherState.Loading(false)
                    _weatherState.value = GetWeatherState.Fail(exception.message.default())
                }
                .collect {
                    _weatherState.value = GetWeatherState.Loading(false)
                    _weatherState.value = GetWeatherState.Success(it)
                }
        }
    }

    fun getLocationWeather(lat: Double, lon: Double, lang: String, units: String, appId: String) {
        viewModelScope.launch {
            getWeatherUseCase.getLocationWeather(lat, lon, lang, units, appId)
                .onStart {
                    _weatherState.value = GetWeatherState.Loading(true)
                }
                .catch { exception ->
                    _weatherState.value = GetWeatherState.Loading(false)
                    _weatherState.value = GetWeatherState.Fail(exception.message.default())
                }
                .collect {
                    _weatherState.value = GetWeatherState.Loading(false)
                    _weatherState.value = GetWeatherState.Success(it)
                }
        }
    }
}