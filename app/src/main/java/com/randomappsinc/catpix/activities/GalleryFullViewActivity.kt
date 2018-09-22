package com.randomappsinc.catpix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnPageChange
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.adapters.GalleryFullViewAdapter
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.persistence.database.FavoritesDataManager

class GalleryFullViewActivity : AppCompatActivity() {

    companion object {
        const val PICTURES_KEY = "urls"
        const val POSITION_KEY = "position"
    }

    @BindView(R.id.pictures_pager) internal lateinit var picturesPager: ViewPager
    @BindView(R.id.favorite_toggle) internal lateinit var favoriteToggle: TextView

    private lateinit var galleryAdapter: GalleryFullViewAdapter
    private var favoritesDataManager = FavoritesDataManager.instance
    private var isCurrentItemFavorited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_full_view)
        ButterKnife.bind(this)

        val pictures = intent.getParcelableArrayListExtra<CatPicture>(PICTURES_KEY)
        galleryAdapter = GalleryFullViewAdapter(supportFragmentManager, pictures)
        picturesPager.offscreenPageLimit = 2
        picturesPager.adapter = galleryAdapter

        val initialPosition = intent.getIntExtra(POSITION_KEY, 0)
        picturesPager.currentItem = initialPosition
        if (initialPosition == 0) {
            refreshFavoritesToggle()
        }
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

    @OnClick(R.id.favorite_toggle)
    fun toggleFavorite() {
        val catPicture = galleryAdapter.pictures[picturesPager.currentItem]
        if (isCurrentItemFavorited) {
            favoritesDataManager.removeFavorite(catPicture)
        } else {
            favoritesDataManager.addFavorite(catPicture)
        }
        isCurrentItemFavorited = !isCurrentItemFavorited
        favoriteToggle.setText(if (isCurrentItemFavorited) R.string.heart_filled_icon else R.string.heart_icon)
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

    @OnClick(R.id.close)
    fun closePage() {
        finish()
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.fade_out)
    }
}
