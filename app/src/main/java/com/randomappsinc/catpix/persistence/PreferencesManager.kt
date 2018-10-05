package com.randomappsinc.catpix.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PreferencesManager(context: Context) {

    companion object {
        private const val NEXT_PAGE_TO_FETCH = "nextPageToFetch"
        private const val NUM_OPENS_KEY = "numOpens"
        private const val RATING_DIALOG_SEEN = "ratingDialogSeen"
        private const val SHARING_DIALOG_SEEN = "sharingDialogSeen"
        private const val TAP_INSTRUCTIONS_SEEN = "tapInstructionsSeen"

        private const val NUM_OPENS_BEFORE_RATING_ASK = 5
        private const val NUM_OPENS_BEFORE_SHARE_ASK = 10
    }

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var nextPageToFetch: Int
        get() = prefs.getInt(NEXT_PAGE_TO_FETCH, 0)
        set(newPage) = prefs.edit().putInt(NEXT_PAGE_TO_FETCH, newPage).apply()

    fun logAppOpen() {
        var currentOpens = prefs.getInt(NUM_OPENS_KEY, 0)
        prefs.edit().putInt(NUM_OPENS_KEY, ++currentOpens).apply()
    }

    fun shouldAskForRating(): Boolean {
        val currentOpens = prefs.getInt(NUM_OPENS_KEY, 0)
        val ratingDialogSeen = prefs.getBoolean(RATING_DIALOG_SEEN, false)
        return currentOpens >= NUM_OPENS_BEFORE_RATING_ASK && !ratingDialogSeen
    }

    fun shouldAskForShare(): Boolean {
        val currentOpens = prefs.getInt(NUM_OPENS_KEY, 0)
        val sharingDialogSeen = prefs.getBoolean(SHARING_DIALOG_SEEN, false)
        return currentOpens >= NUM_OPENS_BEFORE_SHARE_ASK && !sharingDialogSeen
    }

    fun rememberRatingDialogSeen() {
        prefs.edit().putBoolean(RATING_DIALOG_SEEN, true).apply()
    }

    fun rememberSharingDialogSeen() {
        prefs.edit().putBoolean(SHARING_DIALOG_SEEN, true).apply()
    }

    fun shouldShowTapInstructions(): Boolean {
        return !prefs.getBoolean(TAP_INSTRUCTIONS_SEEN, false)
    }

    fun rememberTapInstructionsSeen() {
        prefs.edit().putBoolean(TAP_INSTRUCTIONS_SEEN, true).apply()
    }
}
