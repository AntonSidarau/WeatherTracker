package com.asidarau.wtracker.room

import androidx.room.*
import com.asidarau.wtracker.room.entity.Forecast
import com.asidarau.wtracker.room.entity.ForecastFull


/**
 *
 * @author Anton Sidorov on 19.11.2018.
 */
@Dao
interface ForecastDao {

    @Query("SELECT * FROM forecast")
    fun getAll(): List<Forecast>

    @Query("SELECT * FROM forecast WHERE id = :id")
    fun getById(id: Long): Forecast?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg forecasts: Forecast): List<Long>

    @Delete
    fun delete(forecast: Forecast): Int

    @Query("SELECT * FROM forecast WHERE weather_id = :weatherId")
    fun findForecastsByWeatherId(weatherId: Long): List<Forecast>

    @Transaction
    @Query("SELECT * FROM forecast")
    fun getAllWithParts(): List<ForecastFull>

    @Transaction
    @Query("SELECT * FROM forecast WHERE id = :id")
    fun getByIdWithParts(id: Long): ForecastFull
}
