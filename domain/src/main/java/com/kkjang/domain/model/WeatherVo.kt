package com.kkjang.domain.model

data class WeatherVo(
    val coord: CoordVo,
    val weahter: List<WeatherItemVo>,
    val base: String,
    val main: MainVo,
    val visibility: Int,
    val wind: WindVo,
    val rain: RainVo,
    val clouds: CloudsVo,
    val dt: Long,
    val sys: SysVo,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Int
)

data class CoordVo(
    val lon: Double,
    val lat: Double
)

data class WeatherItemVo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainVo(
    val temp : Double,
    val feels_life: Double,
    val temp_min : Double,
    val temp_max : Double,
    val pressure : Int,
    val humidity : Int,
    val sea_level : Int,
    val grnd_level : Int,
)

data class WindVo(
    val speed : Double,
    val deg : Int,
)

data class RainVo(
    val _1h : Double,
)

data class CloudsVo(
    val all : Int,
)

data class SysVo(
    val type : Int,
    val id : Int,
    val country : String,
    val sunrise : Long,
    val sunset : Long,
)