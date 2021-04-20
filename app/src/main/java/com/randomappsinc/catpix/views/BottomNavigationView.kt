package com.randomappsinc.catpix.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import com.randomappsinc.catpix.R

class BottomNavigationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : LinearLayout(context, attrs, defStyle) {

    private var homeButton: TextView
    private var favoritesButton: TextView
    private var settingsButton: TextView
    private var darkGray: Int = 0
    private var teal: Int = 0

    private var listener: Listener? = null
    private var currentlySelected: TextView? = null

    interface Listener {
        fun onNavItemSelected(@IdRes viewId: Int)
    }

    init {
        View.inflate(getContext(), R.layout.bottom_navigation, this)
        homeButton = findViewById(R.id.home)
        homeButton.setOnClickListener {
            if (currentlySelected === homeButton) {
                return@setOnClickListener
            }

            currentlySelected!!.setTextColor(darkGray)
            currentlySelected = homeButton
            homeButton.setTextColor(teal)
            listener!!.onNavItemSelected(R.id.home)
        }

        favoritesButton = findViewById(R.id.favorites)
        favoritesButton.setOnClickListener {
            if (currentlySelected === favoritesButton) {
                return@setOnClickListener
            }

            currentlySelected!!.setTextColor(darkGray)
            favoritesButton.setTextColor(teal)
            currentlySelected = favoritesButton
            listener!!.onNavItemSelected(R.id.favorites)
        }

        settingsButton = findViewById(R.id.settings)
        settingsButton.setOnClickListener {
            if (currentlySelected === settingsButton) {
                return@setOnClickListener
            }

            currentlySelected!!.setTextColor(darkGray)
            settingsButton.setTextColor(teal)
            currentlySelected = settingsButton
            listener!!.onNavItemSelected(R.id.settings)
        }

        darkGray = ContextCompat.getColor(context, R.color.dark_gray)
        teal = ContextCompat.getColor(context, R.color.app_teal)

        currentlySelected = homeButton
        homeButton.setTextColor(teal)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }
}
