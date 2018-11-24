package com.asidarau.wtracker.domain

import android.util.Log
import com.asidarau.wtracker.data.retrofit.WeatherApi
import com.asidarau.wtracker.domain.model.WeatherDto
import com.asidarau.wtracker.domain.model.WeatherErrorDto
import com.asidarau.wtracker.room.ErrorDao
import com.asidarau.wtracker.room.WeatherDao
import java.io.IOException


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
class WeatherInteractor(
    private val api: WeatherApi,
    private val weatherDao: WeatherDao,
    private val errorDao: ErrorDao
) : Interactor {

    override fun getWeather(latitude: Double, longitude: Double): WeatherDto {
        val response = api.getForecast(latitude, longitude).execute()
        if (response.isSuccessful) {
            val weatherData = response.body()
            return weatherData?.toWeatherDto()
                ?: throw IllegalArgumentException("weather data must not be null")
        } else {
            val code = response.code()
            val message = response.message()
            throw IOException("$code: $message")
        }
    }

    override fun loadFullWeatherInfo(id: Long): WeatherDto {
        try {
            return weatherDao.getFatWeather(id).toWeatherDto()
        } catch (e: Exception) {
            Log.e(TAG, "failed to load fat weather", e)
            throw e
        }
    }

    override fun loadWeathers(): List<WeatherDto> {
        try {
            val weathers = weatherDao.getAllWithForecasts()
            return List(weathers.size) {
                weathers[it].toWeatherDto()
            }
        } catch (e: Exception) {
            Log.e(TAG, "failed to load weather with forecast", e)
            throw e
        }
    }

    override fun loadErrors(): List<WeatherErrorDto> {
        try {
            val errors = errorDao.getAll()
            return List(errors.size) {
                errors[it].toWeatherErrorDto()
            }
        } catch (e: Exception) {
            Log.e(TAG, "failed to load errors")
            throw e
        }
    }

    override fun saveWeather(weatherDto: WeatherDto) {
        try {
            val weather = weatherDto.toWeather()
            val forecast = weatherDto.forecast.toForecast()
            val partDtos = weatherDto.forecast.parts
            val parts = partDtos?.let {
                List(partDtos.size) { index -> partDtos[index].toPart() }
            }

            weatherDao.insertFull(weather, forecast, parts)
        } catch (e: Exception) {
            Log.e(TAG, "faced error to save weather data", e)
            throw e
        }
    }

    override fun logError(error: WeatherErrorDto) {
        try {
            val ids = errorDao.insertAll(error.toWeatherError())
            if (ids.isEmpty()) {
                //no way to handle, because most of the work takes place in background
                throw Exception()
            }
        } catch (e: Exception) {
            Log.e(TAG, "faced error to log in db", e)
        }
    }

    companion object {
        private const val TAG = "WeatherInteractor"
    }
}
