package com.asidarau.wtracker.room.entity

import androidx.room.*
import java.util.*


@Entity(tableName = "weather")
data class Weather(
        @PrimaryKey(autoGenerate = true) override var id: Long? = null,
        @ColumnInfo(name = "now") var now: String,
        @ColumnInfo(name = "now_datetime") var nowDt: Date,
        @Embedded var info: Info,
        @Embedded var fact: Fact
) : BaseEntity

data class Info(
        var latitude: Double,
        var longitude: Double,
        var url: String
)

data class Fact(
        @ColumnInfo(name = "temperature") var temperature: Int,
        @ColumnInfo(name = "feels_like") var feelsLike: Int,
        @ColumnInfo(name = "wind_speed") var windSpeed: Float,
        @ColumnInfo(name = "icon") var icon: String,
        @ColumnInfo(name = "condition") var condition: String,
        @ColumnInfo(name = "wind_gust") var windGust: Float,
        @ColumnInfo(name = "wind_dir") var windDirection: String,
        @ColumnInfo(name = "pressure_mm") var pressureMm: Int,
        @ColumnInfo(name = "pressure_pa") var pressurePa: Int,
        @ColumnInfo(name = "humidity") var humidity: Float,
        @ColumnInfo(name = "daytime") var daytime: String,
        @ColumnInfo(name = "polar") var polar: Boolean,
        @ColumnInfo(name = "season") var season: String,
        @ColumnInfo(name = "obs_time") var observationTime: Long
)

@Entity(
        tableName = "forecast",
        foreignKeys = [ForeignKey(
                entity = Weather::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("weather_id"),
                onDelete = ForeignKey.CASCADE
        )],
        indices = [Index(
                value = ["weather_id"],
                unique = true
        )]
)
data class Forecast(
        @PrimaryKey(autoGenerate = true) override var id: Long? = null,
        @ColumnInfo(name = "weather_id") var weatherId: Long? = null,
        @ColumnInfo(name = "date") var date: String,
        @ColumnInfo(name = "date_ms") var dateMs: Long,
        @ColumnInfo(name = "week") var week: Int,
        @ColumnInfo(name = "sunrise") var sunrise: String,
        @ColumnInfo(name = "sunset") var sunset: String,
        @ColumnInfo(name = "moon_code") var moonCode: Int,
        @ColumnInfo(name = "moon_text") var moonText: String
) : BaseEntity

@Entity(
        tableName = "forecast_part",
        foreignKeys = [ForeignKey(
                entity = Forecast::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("forecast_id"),
                onDelete = ForeignKey.CASCADE
        )],
        indices = [Index(value = ["forecast_id"])]
)
data class Part(
        @PrimaryKey(autoGenerate = true) override var id: Long? = null,
        @ColumnInfo(name = "forecast_id") var forecastId: Long? = null,
        @ColumnInfo(name = "part_name") var partName: String,
        @ColumnInfo(name = "temperature_min") var tempMin: Int,
        @ColumnInfo(name = "temperature_avg") var tempAvg: Int,
        @ColumnInfo(name = "temperature_max") var tempMax: Int,
        @ColumnInfo(name = "feels_like") var feelsLike: Int,
        @ColumnInfo(name = "condition") var condition: String,
        @ColumnInfo(name = "wind_speed") var windSpeed: Float,
        @ColumnInfo(name = "icon") var icon: String,
        @ColumnInfo(name = "daytime") var daytime: String,
        @ColumnInfo(name = "polar") var polar: Boolean,
        @ColumnInfo(name = "wind_gust") var windGust: Float,
        @ColumnInfo(name = "wind_dir") var windDirection: String,
        @ColumnInfo(name = "pressure_mm") var pressureMm: Int,
        @ColumnInfo(name = "pressure_pa") var pressurePa: Int,
        @ColumnInfo(name = "prec_mm") var precInMm: Float,
        @ColumnInfo(name = "prec_period") var precPeriod: Float,
        @ColumnInfo(name = "prec_prob") var precProb: Float,
        @ColumnInfo(name = "humidity") var humidity: Float

) : BaseEntity

class ForecastFull(
        @Embedded var forecast: Forecast
) {
    @Relation(parentColumn = "id", entityColumn = "forecast_id")
    lateinit var parts: List<Part>
}

data class WeatherFull(
        @Embedded var weather: Weather
) {
    @Relation(parentColumn = "id", entityColumn = "weather_id")
    lateinit var forecasts: List<Forecast>
}

data class FatWeather(
        @Embedded var weather: Weather
) {
    @Relation(parentColumn = "id", entityColumn = "weather_id", entity = Forecast::class)
    lateinit var forecast: List<ForecastFull>
}

@Entity(tableName = "error")
data class WeatherError(
        @PrimaryKey(autoGenerate = true) override var id: Long? = null,
        @ColumnInfo(name = "name") var errorName: String,
        @ColumnInfo(name = "time") var date: Date
) : BaseEntity






