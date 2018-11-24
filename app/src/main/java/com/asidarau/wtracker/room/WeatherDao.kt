package com.asidarau.wtracker.room

import androidx.room.*
import com.asidarau.wtracker.room.entity.*


/**
 *
 * @author Anton Sidorov on 19.11.2018.
 */
@Dao
abstract class WeatherDao(db: WeatherDatabase) {

    private val forecastDao = db.getForecastDao()
    private val partDao = db.getPartDao()

    @Query("SELECT * FROM weather")
    abstract fun getAll(): List<Weather>

    @Query("SELECT * FROM weather WHERE id = :id")
    abstract fun getById(id: Long): Weather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg weathers: Weather): List<Long>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFull(weather: Weather, forecast: Forecast, parts: List<Part>?): Long? {
        val weatherId = insertAll(weather)[0]
        forecast.weatherId = weatherId
        val forecastId = forecastDao.insertAll(forecast)[0]
        parts?.let {
            parts.forEach { part -> part.forecastId = forecastId }
            partDao.insertAll(*parts.toTypedArray())
        }

        return weatherId
    }

    @Delete
    abstract fun delete(weather: Weather): Int

    @Transaction
    @Query("SELECT * FROM weather WHERE id = :id")
    abstract fun getFatWeather(id: Long): FatWeather

    @Transaction
    @Query("SELECT * FROM weather")
    abstract fun getAllWithForecasts(): List<WeatherFull>

    @Transaction
    @Query("SELECT * FROM weather WHERE id = :id")
    abstract fun getByIdWithForecasts(id: Long): WeatherFull
}
