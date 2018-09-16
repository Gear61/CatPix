package com.randomappsinc.catpix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.joanzapata.iconify.fonts.IoniconsModule
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.api.CatPicture
import com.randomappsinc.catpix.api.RestClient
import com.randomappsinc.catpix.utils.loadMenuIcon

class MainActivity : AppCompatActivity(), RestClient.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Iconify.with(IoniconsModule())
        setContentView(R.layout.activity_main)

        val restClient = RestClient(this)
        restClient.fetchPictures(1)
    }

    override fun onPicturesFetched(pictures: List<CatPicture>) {
        if (pictures.isEmpty()) {

        }
    }

    override fun onPictureFetchFail() {
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
