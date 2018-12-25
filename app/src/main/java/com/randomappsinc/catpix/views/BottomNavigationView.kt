package com.randomappsinc.catpix.views

import android.content.Context
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindColor
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomappsinc.catpix.R

class BottomNavigationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : LinearLayout(context, attrs, defStyle) {

    @BindView(R.id.home) lateinit var homeButton: TextView
    @BindView(R.id.favorites) lateinit var favoritesButton: TextView
    @BindView(R.id.settings) lateinit var settingsButton: TextView
    @JvmField @BindColor(R.color.dark_gray) var darkGray: Int = 0
    @JvmField @BindColor(R.color.app_teal) var teal: Int = 0

    private var listener: Listener? = null
    private var currentlySelected: TextView? = null

    interface Listener {
        fun onNavItemSelected(@IdRes viewId: Int)
    }

    init {
        View.inflate(getContext(), R.layout.bottom_navigation, this)
        ButterKnife.bind(this)
        currentlySelected = homeButton
        homeButton.setTextColor(teal)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    @OnClick(R.id.home)
    fun onHomeClicked() {
        if (currentlySelected === homeButton) {
            return
        }

        currentlySelected!!.setTextColor(darkGray)
        currentlySelected = homeButton
        homeButton.setTextColor(teal)
        listener!!.onNavItemSelected(R.id.home)
    }

    @OnClick(R.id.favorites)
    fun onSearchClicked() {
        if (currentlySelected === favoritesButton) {
            return
        }

        currentlySelected!!.setTextColor(darkGray)
        favoritesButton.setTextColor(teal)
        currentlySelected = favoritesButton
        listener!!.onNavItemSelected(R.id.favorites)
    }

    @OnClick(R.id.settings)
    fun onProfileClicked() {
        if (currentlySelected === settingsButton) {
            return
        }

        currentlySelected!!.setTextColor(darkGray)
        settingsButton.setTextColor(teal)
        currentlySelected = settingsButton
        listener!!.onNavItemSelected(R.id.settings)
    }
}
