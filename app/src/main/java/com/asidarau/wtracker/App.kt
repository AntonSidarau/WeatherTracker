package com.asidarau.wtracker

import android.app.Application
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.WorkManager
import com.asidarau.wtracker.data.retrofit.WeatherApi
import com.asidarau.wtracker.domain.Interactor
import com.asidarau.wtracker.domain.WeatherInteractor
import com.asidarau.wtracker.presentation.router.WeatherRouter
import com.asidarau.wtracker.room.WeatherDatabase
import com.asidarau.wtracker.workmanager.WeatherWorkerFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 *
 * @author Anton Sidorov on 18.11.2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initRetrofit()
        initRoom()
        initWorkManager()
    }

    private fun initWorkManager() {
        interactor = WeatherInteractor(api, db.getWeatherDao(), db.getErrorDao())
        val configuration = Configuration.Builder()
            .setWorkerFactory(WeatherWorkerFactory(interactor))
            .build()
        WorkManager.initialize(this, configuration)
    }

    private fun initRoom() {
        db = Room.databaseBuilder(this, WeatherDatabase::class.java, "weather-db")
            .build()
    }

    private fun initRetrofit() {
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            clientBuilder.addInterceptor(logging)
        }

        clientBuilder.addInterceptor {
            val request = it
                .request()
                .newBuilder()
                .addHeader(YANDEX_API_HEADER_KEY, YANDEX_API_HEADER_VALUE)
                .build()
            it.proceed(request)
        }

        val gson = GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(clientBuilder.build())
            .build()

        api = retrofit.create(WeatherApi::class.java)
    }

    companion object {

        const val BASE_URL = "https://api.weather.yandex.ru"
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        const val YANDEX_API_HEADER_KEY = "X-Yandex-API-Key"
        const val YANDEX_API_HEADER_VALUE = "1cfbe696-2942-4ac3-9434-cbf3aecfb6d3"

        lateinit var router: WeatherRouter

        private lateinit var db: WeatherDatabase
        private lateinit var api: WeatherApi
        private lateinit var interactor: Interactor


        fun db() = db

        fun api() = api

        fun interactor() = interactor

    }
}
