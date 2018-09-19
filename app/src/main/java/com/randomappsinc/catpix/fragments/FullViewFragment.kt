package com.randomappsinc.catpix.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.utils.showLongToast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class FullViewFragment : Fragment() {

    companion object {
        const val URL_KEY = "url"

        fun newInstance(url: String): FullViewFragment {
            val fragment = FullViewFragment()
            val bundle = Bundle()
            bundle.putString(URL_KEY, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val imageLoadingCallback = object : Callback {
        override fun onSuccess() {
            loadingSpinner.hide()
            picture.animate().alpha(1.0f).duration =
                    resources.getInteger(R.integer.default_anim_length).toLong()
        }

        override fun onError(e: Exception) {
            showLongToast(R.string.image_load_fail, context)
        }
    }

    @BindView(R.id.loading_spinner) internal lateinit var loadingSpinner: ContentLoadingProgressBar
    @BindView(R.id.picture) internal lateinit var picture: ImageView

    private var unbinder: Unbinder? = null
    private lateinit var defaultThumbnail: Drawable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.full_view_fragment, container, false)
        unbinder = ButterKnife.bind(this, rootView)
        loadingSpinner.show()
        defaultThumbnail = IconDrawable(
                activity,
                IoniconsIcons.ion_image).colorRes(R.color.dark_gray)
        val url = arguments!!.getString(URL_KEY)

        Picasso.get()
                .load(url)
                .error(defaultThumbnail)
                .fit()
                .centerInside()
                .into(picture, imageLoadingCallback)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelRequest(picture)
        unbinder!!.unbind()
    }
}
