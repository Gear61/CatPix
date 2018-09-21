package com.randomappsinc.catpix.activities

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.IoniconsModule
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.utils.showHomepageDialog
import com.randomappsinc.catpix.views.BottomNavigationView

class MainActivity : AppCompatActivity() {

    @BindView(R.id.bottom_navigation) lateinit var bottomNavigation: View

    private val bottomNavListener = object : BottomNavigationView.Listener {
        override fun onNavItemSelected(@IdRes viewId: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Iconify.with(IoniconsModule())
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        showHomepageDialog(this)

        BottomNavigationView(bottomNavigation, bottomNavListener)
    }
}
