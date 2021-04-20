package com.randomappsinc.catpix.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.randomappsinc.catpix.fragments.FullViewFragment
import com.randomappsinc.catpix.models.CatPicture

class GalleryFullViewAdapter(
        fragmentManager: FragmentManager,
        val pictures: ArrayList<CatPicture>) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return FullViewFragment.newInstance(pictures[position])
    }

    override fun getCount(): Int {
        return pictures.size
    }
}
