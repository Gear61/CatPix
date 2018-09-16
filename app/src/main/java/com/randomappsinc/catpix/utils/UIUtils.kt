package com.randomappsinc.catpix.utils

import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun closeKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showLongToast(@StringRes stringId: Int, context: Context) {
    showToast(stringId, Toast.LENGTH_LONG, context)
}

fun showToast(@StringRes stringId: Int, toastLength: Int, context: Context) {
    Toast.makeText(context, stringId, toastLength).show()
}
