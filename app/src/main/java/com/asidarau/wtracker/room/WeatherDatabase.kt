package com.asidarau.wtracker.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.asidarau.wtracker.room.entity.Forecast
import com.asidarau.wtracker.room.entity.Part
import com.asidarau.wtracker.room.entity.Weather
import com.asidarau.wtracker.room.entity.WeatherError


/**
 *
 * @author Anton Sidorov on 18.11.2018.
 */
@Database(
    entities = [Part::class, Forecast::class, Weather::class, WeatherError::class],
    version = 2
)
@TypeConverters(WeatherConverters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getPartDao(): PartDao

    abstract fun getForecastDao(): ForecastDao

    abstract fun getWeatherDao(): WeatherDao

    abstract fun getErrorDao(): ErrorDao
}
