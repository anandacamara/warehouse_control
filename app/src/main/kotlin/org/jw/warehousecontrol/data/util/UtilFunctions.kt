package org.jw.warehousecontrol.data.util

import android.util.Base64

internal fun doNothing() {}

internal fun String.convertToBase64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)
}