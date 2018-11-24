package com.asidarau.wtracker.domain.model

import java.util.*


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
data class WeatherDto(
    override val id: Long? = null,
    val now: String,
    val nowDate: Date,
    val info: InfoDto,
    val fact: FactDto,
    val forecast: ForecastDto
) : BaseModel

data class InfoDto(
    val latitude: Double,
    val longitude: Double,
    val url: String
)

data class FactDto(
    val temperature: Int,
    val feelsLike: Int,
    val icon: String,
    val condition: String,
    val windSpeed: Float,
    val windGust: Float,
    val windDirection: String,
    val pressureInMm: Int,
    val pressureInPa: Int,
    val humidity: Float,
    val daytime: String,
    val polar: Boolean,
    val season: String,
    val observationTime: Long
)

data class ForecastDto(
    override val id: Long?,
    val date: String,
    val dateMs: Long,
    val week: Int,
    val sunrise: String,
    val sunset: String,
    val moonCode: Int,
    val moonText: String,
    val parts: List<PartDto>? = null
) : BaseModel

data class PartDto(
    override val id: Long? = null,
    val partName: String,
    val temperatureMin: Int,
    val temperatureMax: Int,
    val temperatureAvg: Int,
    val feelsLike: Int,
    val icon: String,
    val condition: String,
    val daytime: String,
    val polar: Boolean,
    val windSpeed: Float,
    val windGust: Float,
    val windDirection: String,
    val pressureInMm: Int,
    val pressureInPa: Int,
    val humidity: Float,
    val precInMm: Float,
    val precPeriod: Float,
    val precProb: Float
) : BaseModel

data class WeatherErrorDto(
    override val id: Long? = null,
    val errorName: String,
    val date: Date
) : BaseModel

