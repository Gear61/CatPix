package com.randomappsinc.catpix.activities

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnPageChange
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.adapters.GalleryFullViewAdapter
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.persistence.database.FavoritesDataManager
import com.randomappsinc.catpix.utils.Constants
import com.randomappsinc.catpix.utils.animateFavoriteToggle
import com.randomappsinc.catpix.utils.showLongToast
import com.randomappsinc.catpix.utils.showShortToast
import com.randomappsinc.catpix.wallpaper.SetWallpaperManager

class GalleryFullViewActivity : AppCompatActivity(), SetWallpaperManager.SetWallpaperListener {

    companion object {
        const val PICTURES_KEY = "urls"
        const val POSITION_KEY = "position"
        const val FADE_ANIMATION_LENGTH = 250L
    }

    @BindView(R.id.toolbar) internal lateinit var toolbar: View
    @BindView(R.id.pictures_pager) internal lateinit var picturesPager: ViewPager
    @BindView(R.id.favorite_toggle) internal lateinit var favoriteToggle: TextView

    private lateinit var galleryAdapter: GalleryFullViewAdapter
    private var favoritesDataManager= FavoritesDataManager.instance
    private var isCurrentItemFavorited = false
    private var setWallpaperManager = SetWallpaperManager.instance
    private lateinit var fadeOutAnimation: ObjectAnimator
    private lateinit var fadeInAnimation: ObjectAnimator
    private lateinit var progressDialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_full_view)
        ButterKnife.bind(this)

        val pictures = intent.getParcelableArrayListExtra<CatPicture>(PICTURES_KEY)
        galleryAdapter = GalleryFullViewAdapter(supportFragmentManager, pictures!!)
        picturesPager.offscreenPageLimit = 2
        picturesPager.adapter = galleryAdapter

        val initialPosition = intent.getIntExtra(POSITION_KEY, 0)
        picturesPager.currentItem = initialPosition
        if (initialPosition == 0) {
            refreshFavoritesToggle()
        }

        setWallpaperManager.setListener(this)

        fadeOutAnimation = ObjectAnimator.ofFloat(toolbar, "alpha", 0f)
        fadeOutAnimation.duration = FADE_ANIMATION_LENGTH
        fadeOutAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                setWallpaperAfterAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        fadeInAnimation = ObjectAnimator.ofFloat(toolbar, "alpha", 1.0f)
        fadeInAnimation.duration = FADE_ANIMATION_LENGTH

        progressDialog = MaterialDialog(this)
                .customView(R.layout.setting_wallpaper, null, false)
                .cancelable(false)
    }

    private fun refreshFavoritesToggle() {
        val catPicture = galleryAdapter.pictures[picturesPager.currentItem]
        val isFavorited = favoritesDataManager.isPictureFavorited(catPicture)
        isCurrentItemFavorited = isFavorited
        favoriteToggle.setText(if (isFavorited) R.string.heart_filled_icon else R.string.heart_icon)
    }

    @OnPageChange(R.id.pictures_pager)
    fun onImageChanged() {
        refreshFavoritesToggle()
    }

    @OnClick(R.id.set_as_wallpaper)
    fun setAsWallpaperClicked() {
        if (setWallpaperManager.isImageLoaded()) {
            progressDialog.show()
            fadeOutAnimation.start()
        } else {
            showLongToast(R.string.wallpaper_set_too_early, this)
        }
    }

    fun setWallpaperAfterAnimation() {
        setWallpaperManager.setWallpaper(this, R.id.gallery_parent)
    }

    @OnClick(R.id.favorite_toggle)
    fun toggleFavorite() {
        val catPicture = galleryAdapter.pictures[picturesPager.currentItem]
        if (isCurrentItemFavorited) {
            showShortToast(R.string.removed_from_favorites, this)
            favoritesDataManager.removeFavorite(catPicture, Constants.FULL_VIEW)
        } else {
            showShortToast(R.string.added_to_favorites, this)
            favoritesDataManager.addFavorite(catPicture, Constants.FULL_VIEW)
        }
        isCurrentItemFavorited = !isCurrentItemFavorited
        animateFavoriteToggle(favoriteToggle, isCurrentItemFavorited, R.color.white, R.color.white)
    }

    @OnClick(R.id.share)
    fun sharePicture() {
        val currentPosition = picturesPager.currentItem
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(galleryAdapter.pictures[currentPosition].getFullResUrlWithFallback())
                .intent
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(shareIntent)
        }
    }

    override fun onSetWallpaperSuccess() {
        progressDialog.dismiss()
        fadeInAnimation.start()
        showShortToast(R.string.wallpaper_set_success, this)
    }

    override fun onSetWallpaperFail() {
        showLongToast(R.string.wallpaper_set_fail, this)
    }

    @OnClick(R.id.close)
    fun closePage() {
        finish()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            var visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                visibility = visibility or View.SYSTEM_UI_FLAG_IMMERSIVE
            }
            window.decorView.systemUiVisibility = visibility
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in)
    }

    override fun finish() {
        super.finish()
        setWallpaperManager.clearListener()
        overridePendingTransition(0, R.anim.fade_out)
    }
}
