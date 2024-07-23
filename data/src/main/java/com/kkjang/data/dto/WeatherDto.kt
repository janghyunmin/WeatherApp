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
    @SerializedName("name") var name: String?,
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
    @SerializedName("description") var description: String?,
    @SerializedName("icon") var icon: String?
) : Parcelable

@Parcelize
data class MainDto(
    @SerializedName("temp") var temp: Double?,
    @SerializedName("feels_like") var feels_like: Double?,
    @SerializedName("temp_min") var temp_min: Double?,
    @SerializedName("temp_max") var temp_max: Double?,
    @SerializedName("pressure") var pressure: Int?,
    @SerializedName("humidity") var humidity: Int?,
    @SerializedName("sea_level") var sea_level: Int?,
    @SerializedName("grnd_level") var grnd_level: Int?,
) : Parcelable

@Parcelize
data class WindDto(
    @SerializedName("speed") var speed: Double?,
    @SerializedName("deg") var deg: Int?,
    @SerializedName("gust") var gust: Double?
) : Parcelable

@Parcelize
data class RainDto(
    @SerializedName("1h") var _1h: Double?
) : Parcelable

@Parcelize
data class CloudsDto(
    @SerializedName("all") var all: Int?
) : Parcelable

@Parcelize
data class SysDto(
    @SerializedName("type") var type: Int?,
    @SerializedName("id") var id: Int?,
    @SerializedName("country") var country: String?,
    @SerializedName("sunrise") var sunrise: Long?,
    @SerializedName("sunset") var sunset: Long?,
) : Parcelable