package com.randomappsinc.catpix.activities

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.IoniconsModule
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.fragments.HomepageFragmentController
import com.randomappsinc.catpix.persistence.PreferencesManager
import com.randomappsinc.catpix.persistence.database.FavoritesDataManager
import com.randomappsinc.catpix.utils.showHomepageDialog
import com.randomappsinc.catpix.views.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.Listener {

    @BindView(R.id.bottom_navigation) lateinit var bottomNavigation: BottomNavigationView

    private lateinit var navigationController: HomepageFragmentController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Iconify.with(IoniconsModule())
        FavoritesDataManager.instance.initialize(this)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

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
