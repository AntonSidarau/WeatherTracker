package com.asidarau.wtracker.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class PartDaoTest {

    private lateinit var partDao: PartDao
    private lateinit var forecastDao: ForecastDao
    private lateinit var db: WeatherDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        partDao = db.getPartDao()
        forecastDao = db.getForecastDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertPart_insertOnePart_partInserted() {
        val part = EntitiesFactory.providePart()

        val partsAdded = partDao.insertAll(part)
        assertThat(partsAdded.size, `is`(1))

        //hack to test
        part.id = partsAdded[0]

        assertNotNull(part.id)
        val addedPart = partDao.getById(part.id as Long)
        assertThat(addedPart, `is`(part))
    }

    @Test
    fun insertPart_insertMultiple_partsInserted() {
        val partsCount = 3
        val parts = EntitiesFactory.provideParts(partsCount)

        partDao.insertAll(*parts.toTypedArray())

        val partsAdded = partDao.getAll()
        assertThat(partsAdded.size, `is`(partsCount))

        //hack to test
        parts.forEachIndexed { index, part ->
            val addedPart = partsAdded[index]
            assertNotNull(addedPart.id)
            part.id = addedPart.id
            assertThat(part, `is`(addedPart))
        }
    }

    @Test
    fun deletePart_nonNullPart_partDeleted() {
        val part = EntitiesFactory.providePart()
        val ids = partDao.insertAll(part)
        part.id = ids[0]

        val deletedCount = partDao.delete(part)
        assertThat(deletedCount, `is`(1))
        val deletedPart = partDao.getById(part.id as Long)
        assertNull(deletedPart)
    }

    @Test
    fun getPartByForecastId_forecastId_partFounded() {
        val forecast = EntitiesFactory.provideForecast()
        val ids = forecastDao.insertAll(forecast)
        val forecastId = ids[0]

        val part = EntitiesFactory.providePart(forecastId)
        val partIds = partDao.insertAll(part)
        part.id = partIds[0]

        val expected = partDao.findPartsByForecastId(forecastId)
        assertThat(part, `is`(expected[0]))
    }
}
