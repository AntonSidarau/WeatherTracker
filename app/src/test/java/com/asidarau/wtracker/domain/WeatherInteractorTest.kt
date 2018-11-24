package com.asidarau.wtracker.domain

import com.asidarau.wtracker.App
import com.asidarau.wtracker.data.retrofit.WeatherApi
import com.asidarau.wtracker.data.retrofit.model.WeatherData
import com.asidarau.wtracker.domain.model.WeatherErrorDto
import com.asidarau.wtracker.room.EntitiesFactory
import com.asidarau.wtracker.room.ErrorDao
import com.asidarau.wtracker.room.WeatherDao
import com.asidarau.wtracker.room.entity.FatWeather
import com.asidarau.wtracker.room.entity.ForecastFull
import com.asidarau.wtracker.room.entity.WeatherError
import com.asidarau.wtracker.room.entity.WeatherFull
import com.google.gson.GsonBuilder
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
@RunWith(MockitoJUnitRunner::class)
class WeatherInteractorTest {

    @Mock
    private lateinit var api: WeatherApi
    @Mock
    private lateinit var weatherDao: WeatherDao
    @Mock
    private lateinit var errorDao: ErrorDao
    @Mock
    private lateinit var mockedCall: Call<WeatherData>
    @Mock
    private lateinit var mockedResponse: Response<WeatherData>

    private lateinit var interactor: Interactor
    private lateinit var weatherData: WeatherData

    @Before
    fun setUp() {
        interactor = WeatherInteractor(api, weatherDao, errorDao)
        val gson = GsonBuilder()
            .setDateFormat(App.DATE_FORMAT)
            .create()
        weatherData = gson.fromJson(getJson("json/weather_valid_response.json"), WeatherData::class.java)
    }

    @Test
    fun getWeatherData_validParams_successResponse() {
        val actual = weatherData.toWeatherDto()
        `when`(api.getForecast(anyDouble(), anyDouble())).thenReturn(mockedCall)
        `when`(mockedCall.execute()).thenReturn(mockedResponse)
        `when`(mockedResponse.isSuccessful).thenReturn(true)
        `when`(mockedResponse.body()).thenReturn(weatherData)

        val expected = interactor.getWeather(anyDouble(), anyDouble())

        verify(api).getForecast(anyDouble(), anyDouble())
        verify(mockedCall).execute()
        verify(mockedResponse).isSuccessful
        verify(mockedResponse).body()
        assertThat(actual, `is`(expected))
    }

    @Test(expected = IllegalArgumentException::class)
    fun getWeatherData_emptyWeatherData_throwException() {
        `when`(api.getForecast(anyDouble(), anyDouble())).thenReturn(mockedCall)
        `when`(mockedCall.execute()).thenReturn(mockedResponse)
        `when`(mockedResponse.isSuccessful).thenReturn(true)
        `when`(mockedResponse.body()).thenReturn(null)

        interactor.getWeather(anyDouble(), anyDouble())

        verify(api).getForecast(anyDouble(), anyDouble())
        verify(mockedCall).execute()
        verify(mockedResponse).isSuccessful
    }

    @Test(expected = IOException::class)
    fun getWeatherData_httpError_throwException() {
        `when`(api.getForecast(anyDouble(), anyDouble())).thenReturn(mockedCall)
        `when`(mockedCall.execute()).thenReturn(mockedResponse)
        `when`(mockedResponse.isSuccessful).thenReturn(false)

        interactor.getWeather(anyDouble(), anyDouble())

        verify(api).getForecast(anyDouble(), anyDouble())
        verify(mockedCall).execute()
        verify(mockedResponse).isSuccessful
        verify(mockedResponse.body())
        verify(mockedResponse.code())
    }

    @Test
    fun logError_validParams_errorLogged() {
        val stubIds = listOf(1L)
        `when`(errorDao.insertAll(any())).thenReturn(stubIds)

        interactor.logError(WeatherErrorDto(errorName = "name", date = Date()))

        verify(errorDao).insertAll(any())
    }

    @Test
    fun loadWeathers_weathersLoaded() {
        val weatherFull = WeatherFull(EntitiesFactory.provideWeather())
        weatherFull.forecasts = listOf(EntitiesFactory.provideForecast())

        `when`(weatherDao.getAllWithForecasts()).thenReturn(listOf(weatherFull))

        val weathers = interactor.loadWeathers()

        verify(weatherDao).getAllWithForecasts()
        assertThat(weathers.size, `is`(1))
        assertNotNull(weathers[0].forecast)
    }

    @Test(expected = Exception::class)
    fun loadWeathers_errorHappened_throwException() {
        `when`(weatherDao.getAllWithForecasts()).thenThrow(IOException())

        interactor.loadWeathers()
    }

    @Test
    fun loadFullWeatherInfo_validId_weatherDtoReturned() {
        val weather = EntitiesFactory.provideWeather()
        weather.id = 1
        val forecast = EntitiesFactory.provideForecast(1)
        forecast.id = 1
        val parts = EntitiesFactory.provideParts(2, 1)
        val forecastFull = ForecastFull(forecast)
        forecastFull.parts = parts
        val fatWeather = FatWeather(weather)
        fatWeather.forecast = listOf(forecastFull)

        `when`(weatherDao.getFatWeather(anyLong())).thenReturn(fatWeather)

        val weatherDto = interactor.loadFullWeatherInfo(1)

        assertThat(weatherDto.id, `is`(1L))
        assertNotNull(weatherDto.forecast)
        assertNotNull(weatherDto.forecast.parts)
        assertThat(weatherDto.forecast.parts?.size, `is`(2))
    }

    @Test(expected = Exception::class)
    fun loadFullWeatherInfo_errorHappened_throwException() {
        `when`(weatherDao.getFatWeather(anyLong())).thenThrow(IOException())

        interactor.loadFullWeatherInfo(1)
    }

    @Test
    fun loadErrors_errorsLoaded() {
        val mockList = listOf(
            WeatherError(1, "qwe", Date()),
            WeatherError(2, "asd", Date())
        )

        `when`(errorDao.getAll()).thenReturn(mockList)

        val errorDtos = interactor.loadErrors()

        verify(errorDao).getAll()
        assertThat(errorDtos.size, `is`(2))
    }

    @Test(expected = Exception::class)
    fun loadErrors_errorHappened_throwException() {
        `when`(errorDao.getAll()).thenThrow(IOException())

        interactor.loadErrors()
    }

    private fun getJson(path: String): String {
        val uri = javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

}
