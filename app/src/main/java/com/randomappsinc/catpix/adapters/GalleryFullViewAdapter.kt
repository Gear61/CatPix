package com.randomappsinc.catpix.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.randomappsinc.catpix.fragments.FullViewFragment
import com.randomappsinc.catpix.models.CatPicture

class GalleryFullViewAdapter(
        fragmentManager: FragmentManager,
        val pictures: ArrayList<CatPicture>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return FullViewFragment.newInstance(pictures[position])
    }

    override fun getCount(): Int {
        return pictures.size
    }
}
