package com.asidarau.wtracker.data.retrofit.model

import com.google.gson.annotations.SerializedName
import java.util.*


/**
 *
 * @author Anton Sidorov on 20.11.2018.
 */
data class WeatherData(
    @SerializedName("now") val now: String,
    @SerializedName("now_dt") val nowDate: Date,
    @SerializedName("info") val info: InfoData,
    @SerializedName("fact") val fact: FactData,
    @SerializedName("forecast") val forecast: ForecastData
)

data class InfoData(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("url") val url: String
)

data class FactData(
    @SerializedName("temp") val temperature: Int,
    @SerializedName("feels_like") val feelsLike: Int,
    @SerializedName("icon") val icon: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("wind_speed") val windSpeed: Float,
    @SerializedName("wind_gust") val windGust: Float,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("pressure_mm") val pressureInMm: Int,
    @SerializedName("pressure_pa") val pressureInPa: Int,
    @SerializedName("humidity") val humidity: Float,
    @SerializedName("daytime") val daytime: String,
    @SerializedName("polar") val polar: Boolean,
    @SerializedName("season") val season: String,
    @SerializedName("obs_time") val observationTime: Long
)

data class ForecastData(
    @SerializedName("date") val date: String,
    @SerializedName("date_ts") val dateMs: Long,
    @SerializedName("week") val week: Int,
    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("sunset") val sunset: String,
    @SerializedName("mood_code") val moonCode: Int,
    @SerializedName("moon_text") val moonText: String,
    @SerializedName("parts") val parts: List<PartData>
)

data class PartData(
    @SerializedName("part_name") val partName: String,
    @SerializedName("temp_min") val temperatureMin: Int,
    @SerializedName("temp_max") val temperatureMax: Int,
    @SerializedName("temp_avg") val temperatureAvg: Int,
    @SerializedName("feels_like") val feelsLike: Int,
    @SerializedName("icon") val icon: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("daytime") val daytime: String,
    @SerializedName("polar") val polar: Boolean,
    @SerializedName("wind_speed") val windSpeed: Float,
    @SerializedName("wind_gust") val windGust: Float,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("pressure_mm") val pressureInMm: Int,
    @SerializedName("pressure_pa") val pressureInPa: Int,
    @SerializedName("humidity") val humidity: Float,
    @SerializedName("prec_mm") val precInMm: Float,
    @SerializedName("prec_period") val precPeriod: Float,
    @SerializedName("prec_prob") val precProb: Float
)
