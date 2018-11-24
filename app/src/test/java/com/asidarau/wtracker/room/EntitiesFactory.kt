package com.asidarau.wtracker.room

import com.asidarau.wtracker.room.entity.*
import java.time.Instant
import java.util.*


/**
 *
 * @author Anton Sidorov on 19.11.2018.
 */
class EntitiesFactory {

    companion object {

        fun providePart(forecastId: Long? = null) = Part(
            forecastId = forecastId,
            partName = "morning",
            tempMin = 20,
            tempAvg = 21,
            tempMax = 21,
            feelsLike = 23,
            condition = "cloudy",
            windSpeed = 1.2F,
            pressureMm = 777,
            pressurePa = 984,
            icon = "wewrewr",
            daytime = "morning",
            polar = true,
            windGust = 3.6F,
            windDirection = "tuda",
            precInMm = 5F,
            precPeriod = 23F,
            precProb = 12F,
            humidity = 22F
        )

        fun provideParts(count: Int, forecastId: Long? = null) = List(count) {
            Part(
                forecastId = forecastId,
                partName = "name $it",
                tempMin = 20 - it,
                tempAvg = 20,
                tempMax = 20 - it,
                feelsLike = 20 + (it / 2),
                condition = "condition $it",
                windSpeed = 0.3F * it,
                pressureMm = 770 + it,
                pressurePa = 98 + it,
                icon = "wewrewr",
                daytime = "morning",
                polar = true,
                windGust = 0.3F * it,
                windDirection = "tuda",
                precInMm = 5F + it,
                precPeriod = 23F + it,
                precProb = 12F + it,
                humidity = 15F + it
            )
        }

        fun provideForecast(weatherId: Long? = null) = Forecast(
            weatherId = weatherId,
            date = "05-05-2018",
            dateMs = 242342342342L,
            week = 4,
            sunrise = "04:04",
            sunset = "20:32",
            moonCode = 123,
            moonText = "text"
        )

        fun provideForecasts(count: Int, weatherId: Long? = null) = List(count) {
            Forecast(
                weatherId = weatherId,
                date = "05-05-2018",
                dateMs = 242342342342L,
                week = it,
                sunrise = "04:04",
                sunset = "20:32",
                moonCode = it,
                moonText = "text"
            )
        }

        fun provideWeather() = Weather(
            now = "123123124",
            nowDt = Date.from(Instant.now()),
            info = Info(55.3424, 34.32523, "https://yandex.ru/pogoda/minsk"),
            fact = Fact(
                22,
                23,
                0.5F,
                "qwee",
                "condition",
                2.4F,
                "tuda",
                777,
                960,
                4F,
                "summer",
                true,
                "erwer",
                22L
            )
        )

        fun provideWeathers(count: Int) = List(count) {
            Weather(
                now = "1232_$it",
                nowDt = Date.from(Instant.now()),
                info = Info(50.233 + it, 40.34 - it, "https://yandex.ru/pogoda/minsk"),
                fact = Fact(
                    20 + it,
                    21 + it,
                    0.7F,
                    "qwee",
                    "condition",
                    2.4F + it,
                    "tuda",
                    770 + it,
                    960,
                    it.toFloat(),
                    "summer",
                    true,
                    "erwer",
                    22L + it
                )
            )
        }

        fun provideError(count: Int) = List(count) {
            WeatherError(
                errorName = "name$it",
                date = Date()
            )
        }
    }
}
