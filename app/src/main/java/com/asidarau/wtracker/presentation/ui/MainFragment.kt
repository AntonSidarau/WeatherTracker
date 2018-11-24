package com.asidarau.wtracker.presentation.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.asidarau.wtracker.App
import com.asidarau.wtracker.R
import com.asidarau.wtracker.workmanager.WeatherWorker
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import java.util.concurrent.TimeUnit


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnStartWork.setOnClickListener {
            launchWork()
        }

        btnStopWork.setOnClickListener {
            stopWork()
        }

        btnGoToList.setOnClickListener {
            App.router.goToList()
        }
    }

    private fun launchWork() {
        val weatherConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        val weatherWork = PeriodicWorkRequest.Builder(
            WeatherWorker::class.java,
            REPEAT_INTERVAL, TimeUnit.MINUTES,
            FLEX_INTERVAL, TimeUnit.MILLISECONDS
        )
            .setConstraints(weatherConstraints)
            .build()

        val id = weatherWork.id
        WorkManager.getInstance().enqueue(weatherWork)

        val prefs = context?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)?.edit()
        prefs?.run {
            putString(WORK_ID, id.toString())
            prefs.apply()
        }

    }

    private fun stopWork() {
        val prefs = context?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        prefs?.run {
            val id = UUID.fromString(getString(WORK_ID, "0"))
            id.let {
                WorkManager.getInstance().cancelWorkById(it)
            }
        }
    }

    companion object {

        const val REPEAT_INTERVAL = 60L
        const val FLEX_INTERVAL = PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
        const val TAG = "MainFragment"

        private const val SHARED_PREFS = "weather_prefs"
        private const val WORK_ID = "work_id"

        fun newInstance() = MainFragment()
    }
}
