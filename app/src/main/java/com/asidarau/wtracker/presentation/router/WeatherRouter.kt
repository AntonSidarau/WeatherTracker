package com.asidarau.wtracker.presentation.router


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
interface WeatherRouter {

    fun goToList()

    fun goToDetails(weatherId: Long)

    fun back()

    fun backToMain()

    fun destroyRouter()
}
