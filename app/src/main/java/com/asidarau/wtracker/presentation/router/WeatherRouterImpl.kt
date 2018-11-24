package com.asidarau.wtracker.presentation.router

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.asidarau.wtracker.R
import com.asidarau.wtracker.presentation.ui.DetailsFragment
import com.asidarau.wtracker.presentation.ui.ListFragment


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
class WeatherRouterImpl(private var activity: FragmentActivity?) : WeatherRouter {

    override fun goToList() {
        activity?.let {
            val fragmentManager = it.supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, ListFragment.newInstance())
                    .addToBackStack(ListFragment.TAG)
                    .commit()
        }
    }

    override fun goToDetails(weatherId: Long) {
        activity?.let {
            val fragmentManager = it.supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, DetailsFragment.newInstance(weatherId))
                    .addToBackStack(DetailsFragment.TAG)
                    .commit()
        }
    }

    override fun backToMain() {
        activity?.let {
            val fragmentManager = it.supportFragmentManager
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun back() {
    }

    override fun destroyRouter() {
        activity = null
    }
}
