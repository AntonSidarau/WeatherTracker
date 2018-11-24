package com.asidarau.wtracker.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.asidarau.wtracker.domain.Interactor
import com.asidarau.wtracker.domain.model.WeatherErrorDto
import java.util.*


/**
 *
 * @author Anton Sidorov on 08.11.2018.
 */
class WeatherWorker(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams) {

    internal lateinit var interactor: Interactor

    override fun doWork(): Result {
        try {
            val weather = interactor.getWeather(LATITUDE, LONGITUDE)
            interactor.saveWeather(weather)
        } catch (e: Exception) {
            Log.e(TAG, "caught exception", e)
            val message = e.message ?: "unknown error"
            interactor.logError(WeatherErrorDto(errorName = message, date = Date()))
            return Result.RETRY
        }

        return Result.SUCCESS
    }

    companion object {
        private const val TAG = "WeatherWorker"
        private const val LATITUDE = 53.908174
        private const val LONGITUDE = 27.533190
    }
}
