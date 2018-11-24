package com.asidarau.wtracker.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asidarau.wtracker.App
import com.asidarau.wtracker.R
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.concurrent.Executors


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = WeatherAdapter {
            App.router.goToDetails(it)
        }
        rvWeather.layoutManager = LinearLayoutManager(context)
        rvWeather.adapter = adapter
        rvWeather.setHasFixedSize(true)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler()
        executor.submit {
            val data = App.interactor().loadWeathers()
                .plus(App.interactor().loadErrors())

            handler.post {
                adapter.setItems(data)
                executor.shutdown()
            }
        }
    }

    companion object {

        const val TAG = "ListFragment"

        fun newInstance() = ListFragment()
    }
}
