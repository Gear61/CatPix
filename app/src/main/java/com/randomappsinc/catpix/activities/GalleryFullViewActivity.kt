package com.randomappsinc.catpix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.adapters.GalleryFullViewAdapter

class GalleryFullViewActivity : AppCompatActivity() {

    companion object {
        const val URLS_KEY = "urls"
        const val POSITION_KEY = "position"
    }

    @BindView(R.id.pictures_pager) internal lateinit var picturesPager: ViewPager

    private lateinit var galleryAdapter: GalleryFullViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_full_view)
        ButterKnife.bind(this)

        val urls = intent.getStringArrayListExtra(URLS_KEY)
        galleryAdapter = GalleryFullViewAdapter(supportFragmentManager, urls)
        picturesPager.adapter = galleryAdapter

        val initialPosition = intent.getIntExtra(POSITION_KEY, 0)
        picturesPager.currentItem = initialPosition
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