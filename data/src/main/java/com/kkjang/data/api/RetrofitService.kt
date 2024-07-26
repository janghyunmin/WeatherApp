package com.kkjang.data.api

import com.kkjang.data.R
import com.kkjang.data.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    // 날씨 API ( 지역을 영어로 Static 입력 후 사용해야합니다 )
    // ex) data/2.5/weather/?q=Seoul&appid=API_KEY
    @GET("data/2.5/weather")
    suspend fun getWeatherAPI(
        @Query("q") q: String,
        @Query("appid") appId: String
    ) : WeatherDto

    // 날씨 API ( 위도 , 경도 )
    // ex) data/2.5/weather/?lat=37.5665&lon=126.9780&appid=API_KEY
    @GET("data/2.5/weather")
    suspend fun getLocationWeatherAPI(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ) : WeatherDto



    companion object {
        const val BASE_URL = "http://api.openweathermap.org/"
        const val API_KEY = "704ebb3687ad13b8fcc4bc35cb64941c"
        const val IMG_URL = "https://openweathermap.org/img/wn/"
    }
}