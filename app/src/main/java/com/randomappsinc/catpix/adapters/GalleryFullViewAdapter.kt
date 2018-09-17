package com.randomappsinc.catpix.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.randomappsinc.catpix.fragments.FullViewFragment

class GalleryFullViewAdapter(fragmentManager: FragmentManager, private val urls: ArrayList<String>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return FullViewFragment.newInstance(urls[position])
    }

    override fun getCount(): Int {
        return urls.size
    }
}
