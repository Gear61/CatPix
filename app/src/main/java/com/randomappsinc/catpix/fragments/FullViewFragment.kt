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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.utils.cancelImageLoading
import com.randomappsinc.catpix.utils.loadFullResImage
import com.randomappsinc.catpix.utils.showLongToast

class FullViewFragment : Fragment() {

    companion object {
        const val CAT_PICTURE_KEY = "url"

        fun newInstance(catPicture: CatPicture): FullViewFragment {
            val fragment = FullViewFragment()
            val bundle = Bundle()
            bundle.putParcelable(CAT_PICTURE_KEY, catPicture)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val imageLoadingCallback = object : RequestListener<Drawable> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean): Boolean {
            showLongToast(R.string.image_load_fail, context)
            return false
        }

        override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean): Boolean {
            loadingSpinner.hide()
            picture.animate().alpha(1.0f).duration =
                    resources.getInteger(R.integer.default_anim_length).toLong()
            return false
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
        val catPicture: CatPicture = arguments!!.getParcelable(CAT_PICTURE_KEY)!!
        loadFullResImage(catPicture, picture, imageLoadingCallback)
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelImageLoading(picture)
        unbinder!!.unbind()
    }
}
