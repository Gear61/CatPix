package com.randomappsinc.catpix.views

import android.support.annotation.IdRes
import android.view.View
import android.widget.TextView
import butterknife.BindColor
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomappsinc.catpix.R

class BottomNavigationView(parent: View, private val listener: Listener) {

    @BindView(R.id.home) lateinit var homeButton: TextView
    @BindView(R.id.favorites) lateinit var favoritesButton: TextView
    @BindView(R.id.settings) lateinit var settingsButton: TextView
    @BindColor(R.color.dark_gray) @JvmField var darkGray: Int = 0
    @BindColor(R.color.app_teal) @JvmField var red: Int = 0
    private var currentlySelected: TextView

    interface Listener {
        fun onNavItemSelected(@IdRes viewId: Int)
    }

    init {
        ButterKnife.bind(this, parent)
        currentlySelected = homeButton
        homeButton.setTextColor(red)
    }

    @OnClick(R.id.home)
    fun onHomeClicked() {
        if (currentlySelected === homeButton) {
            return
        }

        currentlySelected.setTextColor(darkGray)
        currentlySelected = homeButton
        homeButton.setTextColor(red)
        listener.onNavItemSelected(R.id.home)
    }


    @OnClick(R.id.favorites)
    fun onFavoritesClicked() {
        if (currentlySelected === favoritesButton) {
            return
        }

        currentlySelected.setTextColor(darkGray)
        favoritesButton.setTextColor(red)
        currentlySelected = favoritesButton
        listener.onNavItemSelected(R.id.favorites)
    }

    @OnClick(R.id.settings)
    fun onSettingsClicked() {
        if (currentlySelected === settingsButton) {
            return
        }

        currentlySelected.setTextColor(darkGray)
        settingsButton.setTextColor(red)
        currentlySelected = settingsButton
        listener.onNavItemSelected(R.id.settings)
    }
}
