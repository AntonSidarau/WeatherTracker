package com.asidarau.wtracker.domain

import com.asidarau.wtracker.data.retrofit.model.*
import com.asidarau.wtracker.domain.model.*
import com.asidarau.wtracker.room.entity.*


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
fun WeatherData.toWeatherDto() = WeatherDto(
    null, now, nowDate, info.toInfoDto(), fact.toFactDto(), forecast.toForecastDto()
)

fun InfoData.toInfoDto() = InfoDto(latitude, longitude, url)

fun FactData.toFactDto() = FactDto(
    temperature, feelsLike, icon, condition, windSpeed, windGust, windDirection, pressureInMm,
    pressureInPa, humidity, daytime, polar, season, observationTime
)

fun ForecastData.toForecastDto() = ForecastDto(
    null, date, dateMs, week, sunrise, sunset, moonCode, moonText, List(parts.size) { index ->
        parts[index].toPartDto()
    }
)

fun PartData.toPartDto() = PartDto(
    null, partName, temperatureMin, temperatureMax, temperatureAvg, feelsLike, icon,
    condition, daytime, polar, windSpeed, windGust, windDirection, pressureInMm,
    pressureInPa, humidity, precInMm, precPeriod, precProb
)

fun WeatherErrorDto.toWeatherError() = WeatherError(id, errorName, date)


fun WeatherDto.toWeather() = Weather(id, now, nowDate, info.toInfo(), fact.toFact())

fun InfoDto.toInfo() = Info(latitude, longitude, url)

fun FactDto.toFact() = Fact(
    temperature, feelsLike, windSpeed, icon, condition, windGust,
    windDirection, pressureInMm, pressureInPa, humidity, daytime, polar, season, observationTime
)


fun ForecastDto.toForecast() = Forecast(
    id = id,
    date = date,
    dateMs = dateMs,
    week = week,
    sunrise = sunrise,
    sunset = sunset,
    moonCode = moonCode,
    moonText = moonText
)

fun PartDto.toPart() = Part(
    id = id,
    partName = partName,
    tempMin = temperatureMin,
    tempAvg = temperatureAvg,
    tempMax = temperatureMax,
    feelsLike = feelsLike,
    condition = condition,
    windSpeed = windSpeed,
    pressureMm = pressureInMm,
    pressurePa = pressureInPa,
    icon = icon,
    daytime = daytime,
    polar = polar,
    windGust = windGust,
    windDirection = windDirection,
    precProb = precProb,
    precPeriod = precPeriod,
    precInMm = precInMm,
    humidity = humidity
)

fun Info.toInfoDto() = InfoDto(latitude, longitude, url)

fun Fact.toFactDto() = FactDto(
    temperature, feelsLike, icon, condition, windSpeed, windGust,
    windDirection, pressureMm, pressurePa, humidity, daytime, polar, season, observationTime
)

fun Part.toPartDto() = PartDto(
    id = id,
    partName = partName,
    temperatureMin = tempMin,
    temperatureMax = tempMax,
    temperatureAvg = tempAvg,
    feelsLike = feelsLike,
    icon = icon,
    condition = condition,
    daytime = daytime,
    polar = polar,
    windSpeed = windSpeed,
    windGust = windGust,
    windDirection = windDirection,
    precInMm = precInMm,
    pressureInMm = pressureMm,
    pressureInPa = pressurePa,
    precPeriod = precPeriod,
    precProb = precProb,
    humidity = humidity
)

fun Forecast.toForecastDto(parts: List<Part>? = null) = ForecastDto(
    id, date, dateMs, week, sunrise, sunset, moonCode, moonText, parts?.let {
        List(it.size) { index ->
            parts[index].toPartDto()
        }
    }
)

fun WeatherFull.toWeatherDto(): WeatherDto {
    val weather = this.weather
    val forecast = this.forecasts[0]

    return WeatherDto(
        weather.id, weather.now, weather.nowDt, weather.info.toInfoDto(),
        weather.fact.toFactDto(), forecast.toForecastDto()
    )
}

fun FatWeather.toWeatherDto(): WeatherDto {
    val weather = this.weather
    val forecastFull = this.forecast[0]
    val forecast = forecastFull.forecast
    val parts = forecastFull.parts

    return WeatherDto(
        weather.id, weather.now, weather.nowDt, weather.info.toInfoDto(),
        weather.fact.toFactDto(), forecast.toForecastDto(parts)
    )
}

fun WeatherError.toWeatherErrorDto() = WeatherErrorDto(id, errorName, date)
