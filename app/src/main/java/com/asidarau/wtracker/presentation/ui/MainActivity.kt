package com.asidarau.wtracker.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asidarau.wtracker.App
import com.asidarau.wtracker.R
import com.asidarau.wtracker.presentation.router.WeatherRouterImpl

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            App.router = WeatherRouterImpl(this)
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment.newInstance(), MainFragment.TAG)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            App.router.destroyRouter()
            App.db().close()
        }
    }
}
