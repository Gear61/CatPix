package com.randomappsinc.catpix.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.randomappsinc.catpix.R

class FavoritesFragment : Fragment() {

    fun newInstance(): FavoritesFragment {
        val fragment = FavoritesFragment()
        fragment.retainInstance = true
        return fragment
    }

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorites, container, false)
        unbinder = ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}