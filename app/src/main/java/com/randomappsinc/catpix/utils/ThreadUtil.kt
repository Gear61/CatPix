package com.randomappsinc.catpix.utils

import android.os.Looper

fun assertOnNonUiThread() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        throw RuntimeException("We shouldn't be running this operation on the UI thread!")
    }
}
