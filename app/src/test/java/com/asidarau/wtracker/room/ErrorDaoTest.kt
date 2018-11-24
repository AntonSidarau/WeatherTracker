package com.asidarau.wtracker.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asidarau.wtracker.room.entity.BaseEntity
import com.asidarau.wtracker.room.entity.WeatherError
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
@RunWith(AndroidJUnit4::class)
class ErrorDaoTest {

    private lateinit var errorDao: ErrorDao
    private lateinit var db: WeatherDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        errorDao = db.getErrorDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertError_insertSingle_errorInserted() {
        val error = insertSingleError()

        val expected = errorDao.getById(error.id as Long)

        assertThat(error, `is`(expected))
    }

    @Test
    fun insertError_insertMultiple_errorInserted() {
        val errorCount = 3
        val errors = EntitiesFactory.provideError(errorCount)

        val ids = errorDao.insertAll(*errors.toTypedArray())
        assertThat(ids.size, `is`(errorCount))

        val insertedErrors = errorDao.getAll()
        insertedErrors.checkWithActual(errors, ids)
    }

    @Test
    fun deleteError_nonNullError_errorDeleted() {
        val error = insertSingleError()

        val deletedCount = errorDao.delete(error)
        assertThat(deletedCount, `is`(1))

        val expected = errorDao.getById(error.id as Long)
        assertNull(expected)
    }


    private fun insertSingleError(): WeatherError {
        val error = EntitiesFactory.provideError(1)[0]
        val ids = errorDao.insertAll(error)
        assertThat(ids.size, `is`(1))

        val errorId = ids[0]
        assertNotNull(errorId)
        error.id = errorId

        return error
    }

    private fun <T : BaseEntity> List<T>.checkWithActual(actual: List<T>, ids: List<Long>) {
        this.forEachIndexed { index, expected ->
            val id = ids[index]
            assertNotNull(id)
            actual[index].id = ids[index]
            assertThat(actual[index], CoreMatchers.`is`(expected))
        }
    }
}
