package com.randomappsinc.catpix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.joanzapata.iconify.fonts.IoniconsModule
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.adapters.HomeFeedAdapter
import com.randomappsinc.catpix.api.CatPicture
import com.randomappsinc.catpix.api.RestClient
import com.randomappsinc.catpix.utils.loadMenuIcon

class MainActivity : AppCompatActivity(), RestClient.Listener, HomeFeedAdapter.ItemSelectionListener {

    @BindView(R.id.skeleton_photos) lateinit var skeleton: View
    @BindView(R.id.cat_pictures_list) lateinit var catPicturesList: RecyclerView

    private var feedAdapter : HomeFeedAdapter = HomeFeedAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Iconify.with(IoniconsModule())
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        catPicturesList.adapter = feedAdapter

        val restClient = RestClient(this)
        restClient.fetchPictures(1)
    }

    override fun onPicturesFetched(pictures: List<CatPicture>) {
        val pictureUrls = ArrayList<String>()
        for (catPicture in pictures) {
            val pictureUrl = catPicture.url
            if (pictureUrl != null) {
                pictureUrls.add(pictureUrl)
            }
        }
        skeleton.visibility = View.GONE
        feedAdapter.addPicturesUrls(pictureUrls)
    }

    override fun onPictureFetchFail() {
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, GalleryFullViewActivity::class.java)
                .putStringArrayListExtra(GalleryFullViewActivity.URLS_KEY, feedAdapter.pictureUrls)
                .putExtra(GalleryFullViewActivity.POSITION_KEY, position))
        overridePendingTransition(0, 0)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        loadMenuIcon(menu, R.id.settings, IoniconsIcons.ion_android_settings, this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return false
    }
}
