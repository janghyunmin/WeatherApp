package com.kkjang.data.mapper

import com.kkjang.data.dto.CloudsDto
import com.kkjang.data.dto.CoordDto
import com.kkjang.data.dto.MainDto
import com.kkjang.data.dto.RainDto
import com.kkjang.data.dto.SysDto
import com.kkjang.data.dto.WeatherDto
import com.kkjang.data.dto.WeatherItemDto
import com.kkjang.data.dto.WindDto
import com.kkjang.data.util.default
import com.kkjang.domain.model.CloudsVo
import com.kkjang.domain.model.CoordVo
import com.kkjang.domain.model.MainVo
import com.kkjang.domain.model.RainVo
import com.kkjang.domain.model.SysVo
import com.kkjang.domain.model.WeatherItemVo
import com.kkjang.domain.model.WeatherVo
import com.kkjang.domain.model.WindVo

fun WeatherDto.mapperToWeatherVo(): WeatherVo =
    WeatherVo(
        coord = this.coordDto.mapperToCoordVo(),
        weahter = this.weather.mapperToWeatherItemVo(),
        base = this.base.default(),
        main = this.main.mapperToMainVo(),
        visibility = this.visibility.default(),
        wind = this.wind.mapperToWindVo(),
        rain = this.rain.mapperToRainVo(),
        clouds = this.clouds.mapperToCloudsVo(),
        dt = this.dt.default(),
        sys = this.sys.mapperToSysVo(),
        timezone = this.timezone.default(),
        id = this.id.default(),
        name = this.name.default(),
        cod = this.cod.default()
    )

fun CoordDto?.mapperToCoordVo(): CoordVo =
    CoordVo(
        lon = this?.lon.default(),
        lat = this?.lat.default()
    )

fun List<WeatherItemDto>?.mapperToWeatherItemVo(): List<WeatherItemVo> {
    val list = arrayListOf<WeatherItemVo>()
    this?.let {
        forEach {
            list.add(it.mapperToWeatherItemVo())
        }
    }

    return list
}

fun WeatherItemDto.mapperToWeatherItemVo(): WeatherItemVo = WeatherItemVo(
    id = this.id.default(),
    main = this.main.default(),
    description = this.description.default(),
    icon = this.icon.default()
)

fun MainDto?.mapperToMainVo(): MainVo = MainVo(
    temp = this?.temp.default(),
    feels_life = this?.feels_like.default(),
    temp_min = this?.temp_min.default(),
    temp_max = this?.temp_max.default(),
    pressure = this?.pressure.default(),
    humidity = this?.humidity.default(),
    sea_level = this?.sea_level.default(),
    grnd_level = this?.grnd_level.default()
)

fun WindDto?.mapperToWindVo(): WindVo = WindVo(
    speed = this?.speed.default(),
    deg = this?.deg.default()
)

fun RainDto?.mapperToRainVo(): RainVo = RainVo(
    _1h = this?._1h.default()
)

fun CloudsDto?.mapperToCloudsVo(): CloudsVo = CloudsVo(
    all = this?.all.default()
)

fun SysDto?.mapperToSysVo(): SysVo = SysVo(
    type = this?.type.default(),
    id = this?.id.default(),
    country = this?.country.default(),
    sunrise = this?.sunrise.default(),
    sunset = this?.sunset.default()
)

