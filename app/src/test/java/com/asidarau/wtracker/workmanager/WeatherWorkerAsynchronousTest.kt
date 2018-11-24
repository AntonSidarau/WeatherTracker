package com.asidarau.wtracker.workmanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.testing.WorkManagerTestInitHelper
import com.asidarau.wtracker.domain.StubWeatherInteractor
import com.google.common.util.concurrent.ListenableFuture
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.Executors


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class WeatherWorkerAsynchronousTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val interactor = StubWeatherInteractor()
    private var isInitialized = false

    private fun <V> ListenableFuture<V>.addListenerOnMain(command: () -> Unit) {
        addListener(command, { it?.run() })
    }

    @Before
    fun initWorkManager() {
        if (!isInitialized) {
            val configuration = Configuration.Builder()
                .setWorkerFactory(WeatherWorkerFactory(interactor))
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
            WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
            isInitialized = true
        }
    }

    @Test
    fun cancelWork_validId_workCancelled() {
        interactor.isEmulateWork = true
        val weatherWork = getWeatherWorkRequest()
        WorkManager.getInstance().enqueue(weatherWork)
        val workId = weatherWork.id

        val testDriver = WorkManagerTestInitHelper.getTestDriver()
        testDriver.setInitialDelayMet(workId)
        testDriver.setAllConstraintsMet(workId)

        val operation = WorkManager.getInstance().cancelWorkById(workId)
        operation.result.addListenerOnMain {
            Assert.assertThat(operation.result.isDone, CoreMatchers.`is`(true))
        }

        val status = WorkManager.getInstance().getWorkInfoById(workId)
        Assert.assertThat(status.get().state, CoreMatchers.`is`(WorkInfo.State.CANCELLED))
    }

    private fun getWeatherWorkRequest(): WorkRequest {
        val weatherConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        return OneTimeWorkRequest.Builder(WeatherWorker::class.java)
            .setConstraints(weatherConstraints)
            .build()
    }
}
