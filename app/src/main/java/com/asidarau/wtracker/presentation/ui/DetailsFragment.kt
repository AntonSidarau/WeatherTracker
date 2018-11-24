package com.asidarau.wtracker.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asidarau.wtracker.App
import com.asidarau.wtracker.R
import kotlinx.android.synthetic.main.fragment_details.*
import java.util.concurrent.Executors


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
class DetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnToMain.setOnClickListener {
            App.router.backToMain()
        }

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler()
        executor.submit {
            arguments?.let {
                val weatherFull = App.interactor().loadFullWeatherInfo(it.getLong(WEATHER_ID_KEY))
                handler.post {
                    tvDetails.text = weatherFull.toString()
                    executor.shutdown()
                }
            }

        }
    }

    companion object {

        const val TAG = "DetailsFragment"
        private const val WEATHER_ID_KEY = "weather_id_key"

        fun newInstance(weatherId: Long) = DetailsFragment().apply {
            val arguments = Bundle()
            arguments.putLong(WEATHER_ID_KEY, weatherId)
            setArguments(arguments)
        }
    }
}
