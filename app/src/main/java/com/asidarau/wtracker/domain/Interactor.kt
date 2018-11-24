package com.asidarau.wtracker.domain

import com.asidarau.wtracker.domain.model.WeatherDto
import com.asidarau.wtracker.domain.model.WeatherErrorDto


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
interface Interactor {

    fun getWeather(latitude: Double, longitude: Double): WeatherDto

    fun loadWeathers(): List<WeatherDto>

    fun loadFullWeatherInfo(id: Long): WeatherDto

    fun logError(error: WeatherErrorDto)

    fun loadErrors(): List<WeatherErrorDto>

    fun saveWeather(weather: WeatherDto)
}
