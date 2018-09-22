package com.randomappsinc.catpix.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ShareCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
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

    @BindView(R.id.settings_options) lateinit var settingsOptions: RecyclerView
    @BindString(R.string.feedback_subject) lateinit var feedbackSubject: String
    @BindString(R.string.send_email) lateinit var sendEmail: String

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.settings, container, false)
        unbinder = ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
                val uri = Uri.parse("market://details?id=$context.packageName")
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

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}
