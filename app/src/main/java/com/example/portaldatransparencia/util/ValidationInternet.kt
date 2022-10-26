package com.example.portaldatransparencia.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ValidationInternet {

    fun validationInternet(context: Context): Boolean {
        val result: Boolean
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivity.activeNetwork ?: return false
        val getNetwork = connectivity.getNetworkCapabilities(network) ?: return false

        result = when {
            getNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            getNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
        return result
    }
}