package com.asidarau.wtracker.room

import androidx.room.*
import com.asidarau.wtracker.room.entity.WeatherError


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
@Dao
interface ErrorDao {

    @Query("SELECT * FROM error")
    fun getAll(): List<WeatherError>

    @Query("SELECT * FROM error WHERE id = :id")
    fun getById(id: Long): WeatherError

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg error: WeatherError): List<Long>

    @Delete
    fun delete(error: WeatherError): Int
}
