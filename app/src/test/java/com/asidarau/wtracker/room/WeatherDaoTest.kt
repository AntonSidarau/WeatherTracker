package com.asidarau.wtracker.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asidarau.wtracker.room.entity.BaseEntity
import com.asidarau.wtracker.room.entity.Forecast
import com.asidarau.wtracker.room.entity.Weather
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 *
 * @author Anton Sidorov on 20.11.2018.
 */
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var forecastDao: ForecastDao
    private lateinit var partDao: PartDao
    private lateinit var db: WeatherDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        forecastDao = db.getForecastDao()
        weatherDao = db.getWeatherDao()
        partDao = db.getPartDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertWeather_insertOne_weatherInserted() {
        val weather = insertSingleWeather()

        val expected = weatherDao.getById(weather.id as Long)

        assertThat(weather, `is`(expected))
    }

    @Test
    fun insertWeather_insertMultiple_weatherInserted() {
        val weatherCount = 3
        val weathers = EntitiesFactory.provideWeathers(weatherCount)

        val ids = weatherDao.insertAll(*weathers.toTypedArray())
        assertThat(ids.size, `is`(weatherCount))

        val insertedWeathers = weatherDao.getAll()
        insertedWeathers.checkWithActual(weathers, ids)
    }

    @Test
    fun insertFull_insertValidData_fullDataInserted() {
        val weather = EntitiesFactory.provideWeather()
        val forecast = EntitiesFactory.provideForecast()
        val partsCount = 3
        val parts = EntitiesFactory.provideParts(partsCount)

        val id = weatherDao.insertFull(weather, forecast, parts)
        assertNotNull(id)

        val weatherFull = weatherDao.getAllWithForecasts()[0]
        weather.id = weatherFull.weather.id
        assertThat(weatherFull.weather, `is`(weather))
        forecast.id = weatherFull.forecasts[0].id

        val forecastFull = forecastDao.getByIdWithParts(forecast.id as Long)
        assertThat(forecastFull.forecast, `is`(forecast))
        assertThat(forecastFull.parts.size, `is`(partsCount))
        forecastFull.parts.forEachIndexed { index, part ->
            parts[index].id = part.id
            assertThat(parts[index], `is`(part))
        }
    }

    @Test
    fun deleteWeather_nonNullWeather_weatherDeleted() {
        val weather = insertSingleWeather()

        val deletedCount = weatherDao.delete(weather)
        assertThat(deletedCount, `is`(1))
        val expected = weatherDao.getById(weather.id as Long)
        assertNull(expected)
    }

    @Test
    fun getWeatherWithForecasts_getById_weatherFullFound() {
        val weather = insertSingleWeather()
        val weatherId: Long = weather.id as Long
        val forecasts = insertForecasts(2, weatherId)

        val expected = weatherDao.getByIdWithForecasts(weatherId)

        assertThat(weather, `is`(expected.weather))
        forecasts.forEachIndexed { index, forecast ->
            assertThat(forecast, `is`(expected.forecasts[index]))
        }
    }

    @Test
    fun getWeatherWithForecasts_getAll_weatherFullFound() {
        val weather = insertSingleWeather()
        val forecasts = insertForecasts(2, weather.id as Long)

        val expected = weatherDao.getAllWithForecasts()

        assertThat(expected.size, `is`(1))
        val weatherFull = expected[0]
        assertThat(weather, `is`(weatherFull.weather))
        forecasts.forEachIndexed { index, forecast ->
            assertThat(forecast, `is`(weatherFull.forecasts[index]))
        }
    }

    @Test
    fun getFatWeather_validId_fatWeatherFount() {
        val weather = insertSingleWeather()
        val forecast = insertForecasts(1, weather.id as Long)[0]
        val part = EntitiesFactory.providePart(forecast.id as Long)
        part.id = partDao.insertAll(part)[0]

        val expected = weatherDao.getFatWeather(weather.id as Long)
        val expectedForecast = expected.forecast[0]

        assertThat(weather, `is`(expected.weather))
        assertThat(forecast, `is`(expectedForecast.forecast))
        assertThat(1, `is`(expectedForecast.parts.size))
        assertThat(part, `is`(expectedForecast.parts[0]))
    }

    private fun insertSingleWeather(): Weather {
        val weather = EntitiesFactory.provideWeather()
        val ids = weatherDao.insertAll(weather)
        assertThat(ids.size, `is`(1))

        val weatherId = ids[0]
        assertNotNull(weatherId)
        weather.id = weatherId

        return weather
    }

    private fun insertForecasts(forecastCount: Int, weatherId: Long): List<Forecast> {
        val forecasts = EntitiesFactory.provideForecasts(forecastCount, weatherId)
        val ids = forecastDao.insertAll(*forecasts.toTypedArray())
        assertThat(ids.size, `is`(forecastCount))

        val insertedForecasts = forecastDao.getAll()
        insertedForecasts.checkWithActual(forecasts, ids)

        return insertedForecasts
    }

    private fun <T : BaseEntity> List<T>.checkWithActual(actual: List<T>, ids: List<Long>) {
        this.forEachIndexed { index, expected ->
            val id = ids[index]
            assertNotNull(id)
            actual[index].id = ids[index]
            assertThat(actual[index], `is`(expected))
        }
    }
}
