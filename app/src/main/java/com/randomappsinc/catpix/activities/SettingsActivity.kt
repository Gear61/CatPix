package com.randomappsinc.catpix.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.adapters.SettingsAdapter
import com.randomappsinc.catpix.utils.closeKeyboard
import com.randomappsinc.catpix.utils.showLongToast
import com.randomappsinc.catpix.views.SimpleDividerItemDecoration

class SettingsActivity : AppCompatActivity(), SettingsAdapter.ItemSelectionListener {

    companion object {
        const val SUPPORT_EMAIL = "RandomAppsInc61@gmail.com"
        const val OTHER_APPS_URL = "https://play.google.com/store/apps/developer?id=Jchiou+Apps+Inc."
        const val REPO_URL = "https://github.com/Gear61/CatPix"
    }

    @JvmField @BindView(R.id.settings_options) var settingsOptions: RecyclerView? = null
    @JvmField @BindString(R.string.feedback_subject) var feedbackSubject: String? = null
    @JvmField @BindString(R.string.send_email) var sendEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        ButterKnife.bind(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsOptions!!.addItemDecoration(SimpleDividerItemDecoration(this))
        settingsOptions!!.adapter = SettingsAdapter(this, this)
    }

    override fun onItemClick(position: Int) {
        var intent: Intent? = null
        when (position) {
            0 -> {
                val uriText = "mailto:" + SUPPORT_EMAIL + "?subject=" + Uri.encode(feedbackSubject)
                val mailUri = Uri.parse(uriText)
                val sendIntent = Intent(Intent.ACTION_SENDTO, mailUri)
                startActivity(Intent.createChooser(sendIntent, sendEmail))
                return
            }
            1 -> {
                val shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(getString(R.string.share_app_message))
                        .intent
                if (shareIntent.resolveActivity(packageManager) != null) {
                    startActivity(shareIntent)
                }
                return
            }
            2 -> intent = Intent(Intent.ACTION_VIEW, Uri.parse(OTHER_APPS_URL))
            3 -> {
                val uri = Uri.parse("market://details?id=$packageName")
                intent = Intent(Intent.ACTION_VIEW, uri)
                if (packageManager.queryIntentActivities(intent, 0).size <= 0) {
                    showLongToast(R.string.play_store_error, this)
                    return
                }
            }
            4 -> intent = Intent(Intent.ACTION_VIEW, Uri.parse(REPO_URL))
        }
        startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        closeKeyboard(this)
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in)
    }

    override fun finish() {
        closeKeyboard(this)
        super.finish()
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
