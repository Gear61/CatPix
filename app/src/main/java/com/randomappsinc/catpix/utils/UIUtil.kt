package com.randomappsinc.catpix.utils

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.widget.Toast
import com.joanzapata.iconify.Icon
import com.joanzapata.iconify.IconDrawable
import com.randomappsinc.catpix.R

fun showShortToast(@StringRes stringId: Int, context: Context?) {
    showToast(stringId, Toast.LENGTH_SHORT, context)
}

fun showLongToast(@StringRes stringId: Int, context: Context?) {
    showToast(stringId, Toast.LENGTH_LONG, context)
}

fun showToast(@StringRes stringId: Int, toastLength: Int, context: Context?) {
    Toast.makeText(context, stringId, toastLength).show()
}

fun loadMenuIcon(menu: Menu, itemId: Int, icon: Icon, context: Context) {
    menu.findItem(itemId).icon = IconDrawable(context, icon)
            .colorRes(R.color.white)
            .actionBarSize()
}
