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

    private lateinit var toolbar: View
    private lateinit var picturesPager: ViewPager
    private lateinit var favoriteToggle: TextView

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

        toolbar = findViewById(R.id.toolbar)
        picturesPager = findViewById(R.id.pictures_pager)
        picturesPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                refreshFavoritesToggle()
            }
        })

        favoriteToggle = findViewById(R.id.favorite_toggle)
        favoriteToggle.setOnClickListener {
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

        findViewById<View>(R.id.set_as_wallpaper).setOnClickListener {
            if (setWallpaperManager.isImageLoaded()) {
                progressDialog.show()
                fadeOutAnimation.start()
            } else {
                showLongToast(R.string.wallpaper_set_too_early, this)
            }
        }

        findViewById<View>(R.id.share).setOnClickListener {
            val currentPosition = picturesPager.currentItem
            val shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(galleryAdapter.pictures[currentPosition].getFullResUrlWithFallback())
                    .intent
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(shareIntent)
            }
        }

        findViewById<View>(R.id.close).setOnClickListener {
            finish()
        }

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

    fun setWallpaperAfterAnimation() {
        setWallpaperManager.setWallpaper(this, R.id.gallery_parent)
    }

    override fun onSetWallpaperSuccess() {
        progressDialog.dismiss()
        fadeInAnimation.start()
        showShortToast(R.string.wallpaper_set_success, this)
    }

    override fun onSetWallpaperFail() {
        showLongToast(R.string.wallpaper_set_fail, this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            } else {
                val visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                window.decorView.systemUiVisibility = visibility
            }
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
