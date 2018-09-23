package com.randomappsinc.catpix.utils

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

fun showShortToast(@StringRes stringId: Int, context: Context?) {
    showToast(stringId, Toast.LENGTH_SHORT, context)
}

fun showLongToast(@StringRes stringId: Int, context: Context?) {
    showToast(stringId, Toast.LENGTH_LONG, context)
}

fun showToast(@StringRes stringId: Int, toastLength: Int, context: Context?) {
    Toast.makeText(context, stringId, toastLength).show()
}

fun getScreenWidth(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return metrics.widthPixels
}
