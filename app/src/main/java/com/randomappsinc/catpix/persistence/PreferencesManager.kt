package com.randomappsinc.catpix.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PreferencesManager(context: Context) {

    companion object {
        private const val NEXT_PAGE_TO_FETCH = "nextPageToFetch"
    }

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var nextPageToFetch: Int
        get() = prefs.getInt(NEXT_PAGE_TO_FETCH, 0)
        set(newPage) = prefs.edit().putInt(NEXT_PAGE_TO_FETCH, newPage).apply()
}
