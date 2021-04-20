package com.randomappsinc.catpix.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ShareCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.adapters.SettingsAdapter
import com.randomappsinc.catpix.utils.showLongToast
import com.randomappsinc.catpix.views.SimpleDividerItemDecoration

class SettingsFragment : Fragment(), SettingsAdapter.ItemSelectionListener{

    fun newInstance(): SettingsFragment {
        val fragment = SettingsFragment()
        fragment.retainInstance = true
        return fragment
    }

    companion object {
        const val SUPPORT_EMAIL = "RandomAppsInc61@gmail.com"
        const val OTHER_APPS_URL = "https://play.google.com/store/apps/dev?id=9093438553713389916"
        const val REPO_URL = "https://github.com/Gear61/CatPix"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var settingsOptions: RecyclerView
    private lateinit var feedbackSubject: String
    private lateinit var sendEmail: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.settings, container, false)
        toolbar = rootView.findViewById(R.id.toolbar)
        settingsOptions = rootView.findViewById(R.id.settings_option)

        feedbackSubject = getString(R.string.feedback_subject)
        sendEmail = getString(R.string.send_email)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setTitle(R.string.settings)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        settingsOptions.addItemDecoration(SimpleDividerItemDecoration(activity!!))
        settingsOptions.adapter = SettingsAdapter(activity!!, this)
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
                val shareIntent = ShareCompat.IntentBuilder.from(activity!!)
                        .setType("text/plain")
                        .setText(getString(R.string.share_app_message))
                        .intent
                if (shareIntent.resolveActivity(context!!.packageManager) != null) {
                    startActivity(shareIntent)
                }
                return
            }
            2 -> intent = Intent(Intent.ACTION_VIEW, Uri.parse(OTHER_APPS_URL))
            3 -> {
                val packageName = context!!.packageName
                val uri = Uri.parse("market://details?id=$packageName")
                intent = Intent(Intent.ACTION_VIEW, uri)
                if (context!!.packageManager.queryIntentActivities(intent, 0).size <= 0) {
                    showLongToast(R.string.play_store_error, context)
                    return
                }
            }
            4 -> intent = Intent(Intent.ACTION_VIEW, Uri.parse(REPO_URL))
        }
        startActivity(intent)
    }
}
