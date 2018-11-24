package com.asidarau.wtracker.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.asidarau.wtracker.domain.Interactor


/**
 *
 * @author Anton Sidorov on 21.11.2018.
 */
class WeatherWorkerFactory(private val interactor: Interactor) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName).asSubclass(Worker::class.java)
        val constructor = workerClass.getDeclaredConstructor(
            Context::class.java,
            WorkerParameters::class.java
        )
        val instance = constructor.newInstance(appContext, workerParameters)
        when (instance) {
            is WeatherWorker -> instance.interactor = interactor
        }

        return instance
    }
}
