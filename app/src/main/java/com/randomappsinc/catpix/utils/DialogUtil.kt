package com.randomappsinc.catpix.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ShareCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.persistence.PreferencesManager

fun showHomepageDialog(activity: Activity) {
    val preferencesManager = PreferencesManager(activity)
    if (preferencesManager.shouldAskForRating()) {
        preferencesManager.rememberRatingDialogSeen()
        MaterialDialog(activity)
                .message(R.string.please_rate)
                .negativeButton(R.string.no_im_good)
                .positiveButton(R.string.sure_will_help) {
                    val packageName = activity.packageName
                    val uri = Uri.parse("market://details?id=$packageName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    if (activity.packageManager.queryIntentActivities(intent, 0).size <= 0) {
                        showLongToast(R.string.play_store_error, activity)
                        return@positiveButton
                    }
                    activity.startActivity(intent)
                }
                .cancelOnTouchOutside(false)
                .show()
    } else if (preferencesManager.shouldAskForShare()) {
        preferencesManager.rememberSharingDialogSeen()
        MaterialDialog(activity)
                .title(R.string.share_prompt_title)
                .message(R.string.please_share)
                .negativeButton(R.string.no_im_good)
                .positiveButton(R.string.sure_will_help) {
                    val shareIntent = ShareCompat.IntentBuilder.from(activity)
                            .setType("text/plain")
                            .setText(activity.getString(R.string.share_app_message))
                            .intent
                    if (shareIntent.resolveActivity(activity.packageManager) != null) {
                        activity.startActivity(shareIntent)
                    }
                }
                .cancelOnTouchOutside(false)
                .show()
    }
}
