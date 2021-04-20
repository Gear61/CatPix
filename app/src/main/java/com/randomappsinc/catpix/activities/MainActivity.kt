package com.randomappsinc.catpix.activities

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.IoniconsModule
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.fragments.HomepageFragmentController
import com.randomappsinc.catpix.persistence.PreferencesManager
import com.randomappsinc.catpix.persistence.database.FavoritesDataManager
import com.randomappsinc.catpix.utils.showHomepageDialog
import com.randomappsinc.catpix.views.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.Listener {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navigationController: HomepageFragmentController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Iconify.with(IoniconsModule())
        FavoritesDataManager.instance.initialize(this)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        PreferencesManager(this).logAppOpen()
        showHomepageDialog(this)

        navigationController = HomepageFragmentController(supportFragmentManager, R.id.container)
        bottomNavigation.setListener(this)
        navigationController.loadHome()
    }

    override fun onNavItemSelected(@IdRes viewId: Int) {
        navigationController.onNavItemSelected(viewId)
        when (viewId) {
            R.id.home -> setTitle(R.string.app_name)
            R.id.favorites -> setTitle(R.string.favorites)
            R.id.settings -> setTitle(R.string.settings)
        }
    }
}
