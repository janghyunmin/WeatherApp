package com.kkjang.weather.feature.weather

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.kkjang.data.module.provider.ResourcesProvider
import com.kkjang.data.util.default
import com.kkjang.domain.usecase.GetWeatherUseCase
import com.kkjang.weather.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val resourcesProvider: ResourcesProvider,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<GetWeatherState>(GetWeatherState.Init)
    val weatherState = _weatherState.asStateFlow()

    private val _anotherWeatherState = MutableStateFlow<GetAnotherWeatherState>(GetAnotherWeatherState.Init)
    val anotherWeatherState = _anotherWeatherState.asStateFlow()

    private val _searchWeatherStates = MutableStateFlow<Map<String, GetWeatherState>>(emptyMap())
    val searchWeatherStates: StateFlow<Map<String, GetWeatherState>> = _searchWeatherStates


    // 나의 위치 위도 , 경도
    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState = _locationState.asStateFlow()

    // 주소를 저장할 StateFlow
    private val _addressLine = MutableStateFlow<String>("")
    val addressLine: StateFlow<String> get() = _addressLine


    // Fail 시 SnackBar Event
    private val _toastEvent = Channel<String>()
    val toastEvent = _toastEvent.receiveAsFlow()

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


    fun getSearchWeather(location: String, apiKey: String) {
        viewModelScope.launch {
            try {
                getWeatherUseCase.getWeather(q = location, appId = apiKey)
                    .collect {
                        _searchWeatherStates.value += (location to GetWeatherState.Success(it))
                    }
            } catch (e: Exception) {
                _searchWeatherStates.value += (location to GetWeatherState.Fail(e.toString()))
            }
        }
    }

    // Call this function to load weather data for multiple locations
    fun loadWeatherData(apiKey: String) {
        val locations = listOf("Seoul", "Gyeonggi-do", "Gangwon-do", "Busan")
        locations.forEach { location ->
            getSearchWeather(location, apiKey)
        }
    }


    // 위치 기반 날씨 정보 조회
    fun getLocationWeather(lat: Double, lon: Double, lang: String, units: String, appId: String) {
        viewModelScope.launch {
            getWeatherUseCase.getLocationWeather(lat, lon, lang, units, appId)
                .onStart {
                    _anotherWeatherState.value = GetAnotherWeatherState.Loading(true)
                }
                .catch { exception ->
                    _anotherWeatherState.value = GetAnotherWeatherState.Loading(false)
                    _anotherWeatherState.value = GetAnotherWeatherState.Fail(exception.message.default())
                }
                .collect {
                    _anotherWeatherState.value = GetAnotherWeatherState.Loading(false)
                    _anotherWeatherState.value = GetAnotherWeatherState.Success(it)
                }
        }
    }


    @SuppressLint("MissingPermission")
    fun getLocation(activity: MainActivity) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

        fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    _locationState.value = location
                }
            }
            .addOnFailureListener { fail ->
                viewModelScope.launch {
                    _toastEvent.send("Fail : ${fail.message}")
                }
            }
    }

    fun getAddress(context: Context, lat: Double, lng: Double): List<Address>? {
        lateinit var address: List<Address>

        return try {
            val geocoder = Geocoder(context, Locale.KOREA)
            address = geocoder.getFromLocation(lat, lng, 1) as List<Address>
            address
        } catch (e: IOException) {
            null
        }
    }

    fun setAddress(address: String) {
        _addressLine.value = address
    }


}