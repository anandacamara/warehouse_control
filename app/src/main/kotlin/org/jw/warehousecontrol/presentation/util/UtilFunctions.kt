package org.jw.warehousecontrol.presentation.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.Normalizer

/**
 * @author Ananda Camara
 */

internal fun doNothing() {}

internal fun String.toDatabaseLabel(): String {
    return this.lowercase().split(" ").joinToString("-")
}

internal fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork?.let {
        connectivityManager.getNetworkCapabilities(it)
    } ?: return false

    return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}

internal fun String.unaccent(): String {
    val temp = Normalizer.normalize(this.lowercase(), Normalizer.Form.NFD)
    return "\\p{InCombiningDiacriticalMarks}+".toRegex().replace(temp, "")
}