package com.harshit.newsfeed

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import androidx.core.content.ContextCompat.getSystemService

object Constants{
    const val apiKey = "e5fe35700d654b3b9fb4cbebce2824af"
    const val domain = "wsj.com"
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}