package com.asidarau.wtracker.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}


