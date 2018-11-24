package com.asidarau.wtracker.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asidarau.wtracker.room.entity.BaseEntity
import com.asidarau.wtracker.room.entity.Forecast
import com.asidarau.wtracker.room.entity.Part
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 *
 * @author Anton Sidorov on 19.11.2018.
 */
@RunWith(AndroidJUnit4::class)
class ForecastDaoTest {

    private lateinit var forecastDao: ForecastDao
    private lateinit var weatherDao: WeatherDao
    private lateinit var partDao: PartDao
    private lateinit var db: WeatherDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        partDao = db.getPartDao()
        forecastDao = db.getForecastDao()
        weatherDao = db.getWeatherDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertForecast_insertOne_forecastInserted() {
        val forecast = insertSingleForecast()

        val expected = forecastDao.getById(forecast.id as Long)

        assertThat(forecast, `is`(expected))
    }

    @Test
    fun insertForecast_insertMultiple_forecastInserted() {
        val forecastsCount = 3
        val forecasts = EntitiesFactory.provideForecasts(forecastsCount)

        val ids = forecastDao.insertAll(*forecasts.toTypedArray())
        assertThat(ids.size, `is`(forecastsCount))

        val insertedForecasts = forecastDao.getAll()
        insertedForecasts.checkWithActual(forecasts, ids)
    }

    @Test
    fun deleteForecast_nonNullForecast_forecastDeleted() {
        val forecast = insertSingleForecast()

        val deletedCount = forecastDao.delete(forecast)
        assertThat(deletedCount, `is`(1))
        val expected = forecastDao.getById(forecast.id as Long)

        assertNull(expected)
    }

    @Test
    fun getForecastsByWeatherId_weatherId_forecastFounded() {
        val weather = EntitiesFactory.provideWeather()
        val ids = weatherDao.insertAll(weather)
        assertThat(ids.size, `is`(1))

        val weatherId = ids[0]
        assertNotNull(weatherId)
        val forecast = insertSingleForecast(weatherId)

        val expected = forecastDao.findForecastsByWeatherId(weatherId)
        assertThat(forecast, `is`(expected[0]))
    }

    @Test
    fun getForecastWithParts_getById_forecastFullFound() {
        val forecast = insertSingleForecast()
        val parts = insertParts(2, forecast.id as Long)

        val expected = forecastDao.getByIdWithParts(forecast.id as Long)

        assertThat(forecast, `is`(expected.forecast))
        assertThat(parts.size, `is`(expected.parts.size))
        parts.forEachIndexed { index, part ->
            assertThat(part, `is`(expected.parts[index]))
        }
    }

    @Test
    fun getForecastWithParts_getAll_forecastFullFound() {
        val forecast = insertSingleForecast()
        val parts = insertParts(2, forecast.id as Long)

        val expected = forecastDao.getAllWithParts()

        assertThat(expected.size, `is`(1))
        val forecastFull = expected[0]
        assertThat(forecast, `is`(forecastFull.forecast))
        parts.forEachIndexed { index, part ->
            assertThat(part, `is`(forecastFull.parts[index]))
        }
    }

    private fun insertSingleForecast(weatherId: Long? = null): Forecast {
        val forecast = EntitiesFactory.provideForecast(weatherId)
        val ids = forecastDao.insertAll(forecast)
        assertThat(ids.size, `is`(1))

        val forecastId = ids[0]
        assertNotNull(forecastId)
        forecast.id = forecastId

        return forecast
    }

    private fun insertParts(partsCount: Int, forecastId: Long): List<Part> {
        val parts = EntitiesFactory.provideParts(partsCount, forecastId)
        val partsIds = partDao.insertAll(*parts.toTypedArray())
        assertThat(partsIds.size, `is`(partsCount))

        val insertedParts = partDao.getAll()
        assertThat(partsCount, `is`(insertedParts.size))
        insertedParts.checkWithActual(parts, partsIds)

        return insertedParts
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
