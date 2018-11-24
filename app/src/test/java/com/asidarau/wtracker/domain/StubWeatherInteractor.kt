package com.asidarau.wtracker.domain

import com.asidarau.wtracker.App
import com.asidarau.wtracker.data.retrofit.model.WeatherData
import com.asidarau.wtracker.domain.model.WeatherDto
import com.asidarau.wtracker.domain.model.WeatherErrorDto
import com.google.gson.GsonBuilder
import java.io.File
import java.util.*


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
class StubWeatherInteractor(
    var isGetWeatherFailed: Boolean = false,
    var isLogErrorFailed: Boolean = false,
    var isSaveWeatherFailed: Boolean = false,
    var isLoadWeathersFailed: Boolean = false,
    var isEmulateWork: Boolean = false,
    var isLoadFullWeatherInfoFailed: Boolean = false,
    var isLoadErrorsFailed: Boolean = false
) : Interactor {

    override fun loadWeathers(): List<WeatherDto> {
        if (isLoadWeathersFailed) {
            throw Exception()
        }

        return listOf(mockWeatherData.toWeatherDto())
    }

    private var mockWeatherData: WeatherData

    override fun loadFullWeatherInfo(id: Long): WeatherDto {
        if (isLoadFullWeatherInfoFailed) {
            throw Exception()
        }

        return mockWeatherData.toWeatherDto()
    }

    init {
        val gson = GsonBuilder()
            .setDateFormat(App.DATE_FORMAT)
            .create()
        mockWeatherData = gson.fromJson(getJson("json/weather_valid_response.json"), WeatherData::class.java)
    }


    override fun getWeather(latitude: Double, longitude: Double): WeatherDto {
        if (isGetWeatherFailed) {
            throw Exception()
        }

        if (isEmulateWork) {
            Thread.sleep(2000)
        }

        return mockWeatherData.toWeatherDto()
    }

    override fun logError(error: WeatherErrorDto) {
        if (isLogErrorFailed) {
            throw Exception()
        }
    }

    override fun loadErrors(): List<WeatherErrorDto> {
        if (isLoadErrorsFailed) {
            throw Exception()
        }

        return listOf(WeatherErrorDto(1, "qwe", Date()))
    }

    override fun saveWeather(weather: WeatherDto) {
        if (isSaveWeatherFailed) {
            throw Exception()
        }
    }

    private fun getJson(path: String): String {
        val uri = javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}
