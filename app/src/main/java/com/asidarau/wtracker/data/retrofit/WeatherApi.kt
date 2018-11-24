package com.asidarau.wtracker.data.retrofit

import com.asidarau.wtracker.data.retrofit.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 *
 * @author Anton Sidorov on 20.11.2018.
 */
interface WeatherApi {

    @GET("/v1/informers")
    fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Call<WeatherData>
}
