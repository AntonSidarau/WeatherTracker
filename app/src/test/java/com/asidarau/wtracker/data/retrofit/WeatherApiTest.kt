package com.asidarau.wtracker.data.retrofit

import com.asidarau.wtracker.App
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


/**
 *
 * @author Anton Sidorov on 20.11.2018.
 */
class WeatherApiTest {

    private lateinit var webServer: MockWebServer
    private lateinit var weatherApi: WeatherApi

    @Before
    fun setUp() {
        webServer = MockWebServer()
        webServer.start()

        val client = OkHttpClient.Builder()
            .addInterceptor {
                val request = it
                    .request()
                    .newBuilder()
                    .addHeader(App.YANDEX_API_HEADER_KEY, App.YANDEX_API_HEADER_VALUE)
                    .build()
                it.proceed(request)
            }
            .build()

        val gson = GsonBuilder()
            .setDateFormat(App.DATE_FORMAT)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(webServer.url("/").toString())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun getWeather_validRequest_validResponse() {
        val path = "/v1/informers?lat=11.353&lon=34.324"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("json/weather_valid_response.json"))
        webServer.enqueue(mockResponse)

        val expected = weatherApi.getForecast(11.353, 34.324).execute()

        val request = webServer.takeRequest()
        assertThat(request.path, `is`(path))
        assertThat(expected.isSuccessful, `is`(true))
        assertThat(expected.body(), notNullValue())
    }

    private fun getJson(path: String): String {
        val uri = javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}
