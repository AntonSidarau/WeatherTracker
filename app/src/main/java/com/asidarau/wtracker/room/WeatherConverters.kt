package com.asidarau.wtracker.room

import androidx.room.TypeConverter
import java.util.*


/**
 *
 * @author Anton Sidorov on 18.11.2018.
 */
class WeatherConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
