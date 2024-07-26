package com.kkjang.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDto(
    @SerializedName("coord") var coordDto: CoordDto?,
    @SerializedName("weather") var weather: List<WeatherItemDto>?,
    @SerializedName("base") var base: String?,
    @SerializedName("main") var main: MainDto?,
    @SerializedName("visibility") var visibility: Int?,
    @SerializedName("wind") var wind: WindDto?,
    @SerializedName("rain") var rain: RainDto?,
    @SerializedName("clouds") var clouds: CloudsDto?,
    @SerializedName("dt") var dt: Long?,
    @SerializedName("sys") var sys: SysDto?,
    @SerializedName("timezone") var timezone: Long?,
    @SerializedName("id") var id: Long?,
    @SerializedName("name") var name: String?, // 지역 이름
    @SerializedName("cod") var cod: Int?
) : Parcelable


@Parcelize
data class CoordDto(
    @SerializedName("lon") var lon: Double?,
    @SerializedName("lat") var lat: Double?
) : Parcelable

@Parcelize
data class WeatherItemDto(
    @SerializedName("id") var id: Int?,
    @SerializedName("main") var main: String?,
    @SerializedName("description") var description: String?, // 현재 날씨
    @SerializedName("icon") var icon: String?
) : Parcelable

@Parcelize
data class MainDto(
    @SerializedName("temp") var temp: Double?, // 현재 온도
    @SerializedName("feels_like") var feels_like: Double?, // 체감 온도
    @SerializedName("temp_min") var temp_min: Double?, // 최저 기온
    @SerializedName("temp_max") var temp_max: Double?, // 최고 기온
    @SerializedName("pressure") var pressure: Int?, // 기압
    @SerializedName("humidity") var humidity: Int?, // 습도
    @SerializedName("sea_level") var sea_level: Int?,
    @SerializedName("grnd_level") var grnd_level: Int?, // 땅
) : Parcelable

@Parcelize
data class WindDto(
    @SerializedName("speed") var speed: Double?, // 풍속
    @SerializedName("deg") var deg: Int?, // 풍향
    @SerializedName("gust") var gust: Double? // 바람
) : Parcelable

@Parcelize
data class RainDto(
    @SerializedName("1h") var _1h: Double? // 1시간
) : Parcelable

@Parcelize
data class CloudsDto(
    @SerializedName("all") var all: Int? // 구름
) : Parcelable

@Parcelize
data class SysDto(
    @SerializedName("type") var type: Int?,
    @SerializedName("id") var id: Int?,
    @SerializedName("country") var country: String?,
    @SerializedName("sunrise") var sunrise: Long?,
    @SerializedName("sunset") var sunset: Long?,
) : Parcelable