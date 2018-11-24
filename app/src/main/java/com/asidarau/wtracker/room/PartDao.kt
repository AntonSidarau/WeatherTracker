package com.asidarau.wtracker.room

import androidx.room.*
import com.asidarau.wtracker.room.entity.Part


/**
 *
 * @author Anton Sidorov on 18.11.2018.
 */
@Dao
interface PartDao {

    @Query("SELECT * FROM forecast_part")
    fun getAll(): List<Part>

    @Query("SELECT * FROM forecast_part WHERE id = :id")
    fun getById(id: Long): Part?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg parts: Part): List<Long>

    @Delete
    fun delete(part: Part): Int

    @Query("SELECT * FROM forecast_part WHERE forecast_id = :forecastId")
    fun findPartsByForecastId(forecastId: Long) : List<Part>
}
