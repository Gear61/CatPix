package com.randomappsinc.catpix.fragments

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.randomappsinc.catpix.R

class HomepageFragmentController(private val fragmentManager: FragmentManager, private val containerId: Int) {

    private var homeFeedFragment: HomeFeedFragment = HomeFeedFragment().newInstance()
    private var favoritesFragment: FavoritesFragment? = null
    private var settingsFragment: SettingsFragment? = null
    @IdRes private var currentViewId: Int = 0

    fun onNavItemSelected(@IdRes viewId: Int) {
        if (currentViewId == viewId) {
            return
        }

        val fragmentTransaction = fragmentManager.beginTransaction()

        // Hide current fragment
        when (currentViewId) {
            R.id.home -> fragmentTransaction.hide(homeFeedFragment)
            R.id.favorites -> fragmentTransaction.hide(favoritesFragment!!)
            R.id.settings -> fragmentTransaction.hide(settingsFragment!!)
        }

        // Show the fragment the user tabbed to
        when (viewId) {
            R.id.home -> fragmentTransaction.show(homeFeedFragment)
            R.id.favorites -> {
                if (favoritesFragment == null) {
                    favoritesFragment = FavoritesFragment().newInstance()
                    fragmentTransaction.add(containerId, favoritesFragment!!)
                } else {
                    fragmentTransaction.show(favoritesFragment!!)
                }
            }
            R.id.settings -> {
                if (settingsFragment == null) {
                    settingsFragment = SettingsFragment().newInstance()
                    fragmentTransaction.add(containerId, settingsFragment!!)
                } else {
                    fragmentTransaction.show(settingsFragment!!)
                }
            }
        }
        currentViewId = viewId
        fragmentTransaction.commit()
    }

    /** Called by the app upon start up to load the home fragment  */
    fun loadHome() {
        currentViewId = R.id.home
        fragmentManager.beginTransaction().add(containerId, homeFeedFragment).commit()
    }
}
