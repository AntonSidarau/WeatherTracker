package com.asidarau.wtracker.workmanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.testing.WorkManagerTestInitHelper
import com.asidarau.wtracker.domain.StubWeatherInteractor
import com.google.common.util.concurrent.ListenableFuture
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.isOneOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


/**
 *
 * @author Anton Sidorov on 12.11.2018.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = TestApp::class)
class WeatherWorkerSynchronousTest {

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
                .build()
            WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
            isInitialized = true
        }
    }

    @Test
    fun runWork_validId_workIsDone() {
        val weatherWork = getWeatherWorkRequest()

        WorkManager.getInstance().enqueue(weatherWork)
        val workId = weatherWork.id

        val testDriver = WorkManagerTestInitHelper.getTestDriver()
        testDriver.setInitialDelayMet(workId)
        testDriver.setAllConstraintsMet(workId)

        val status = WorkManager.getInstance().getWorkInfoById(workId)
        status.addListenerOnMain {
            if (status.isDone) {
                val workStatus = status.get()
                assertThat(workStatus.state.isFinished, `is`(true))
                assertThat(workStatus.state, `is`(WorkInfo.State.SUCCEEDED))
            }
        }
    }

    @Test
    fun runWork_failedToGetWeather_LogErrorAndRetry() {
        interactor.isGetWeatherFailed = true
        val weatherWork = getWeatherWorkRequest()

        WorkManager.getInstance().enqueue(weatherWork)
        val workId = weatherWork.id

        val testDriver = WorkManagerTestInitHelper.getTestDriver()
        testDriver.setInitialDelayMet(workId)
        testDriver.setAllConstraintsMet(workId)

        val status = WorkManager.getInstance().getWorkInfoById(workId)
        status.addListenerOnMain {
            if (status.isDone) {
                val workStatus = status.get()
                assertThat(workStatus.state, isOneOf(WorkInfo.State.RUNNING, WorkInfo.State.ENQUEUED))
            }
        }
    }


    private fun getConfigurationBuilder(): Configuration.Builder {
        return Configuration.Builder()
            .setWorkerFactory(WeatherWorkerFactory(interactor))
    }

    private fun setUpSynchronousWorkManager() {
        WorkManagerTestInitHelper.initializeTestWorkManager(
            context,
            getConfigurationBuilder().build()
        )
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
