package com.randomappsinc.catpix.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.randomappsinc.catpix.fragments.FullViewFragment
import com.randomappsinc.catpix.models.CatPicture

class GalleryFullViewAdapter(
        fragmentManager: FragmentManager,
        private val pictures: ArrayList<CatPicture>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return FullViewFragment.newInstance(pictures[position].getFullResUrlWithFallback()!!)
    }

    override fun getCount(): Int {
        return pictures.size
    }
}
