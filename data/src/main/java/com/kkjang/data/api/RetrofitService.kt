package com.kkjang.data.api

import com.kkjang.data.R
import com.kkjang.data.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    // 날씨 API
    @GET("data/2.5/weather")
    suspend fun getWeatherAPI(
        @Query("q") q: String,
        @Query("appid") appId: String
    ) : WeatherDto

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/"
        const val API_KEY = "704ebb3687ad13b8fcc4bc35cb64941c"
    }
}