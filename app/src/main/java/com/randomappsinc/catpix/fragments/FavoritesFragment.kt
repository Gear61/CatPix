package com.randomappsinc.catpix.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.activities.GalleryFullViewActivity
import com.randomappsinc.catpix.adapters.FavoritesAdapter
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.persistence.database.FavoritesDataManager

class FavoritesFragment : Fragment(), FavoritesDataManager.ChangeListener,
        FavoritesAdapter.Listener, FavoritesDataManager.FavoritesReceiver {

    fun newInstance(): FavoritesFragment {
        val fragment = FavoritesFragment()
        fragment.retainInstance = true
        return fragment
    }

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.favorites_grid) internal lateinit var favoritesGrid: RecyclerView
    @BindView(R.id.no_results) internal lateinit var noResults: TextView
    @BindView(R.id.favorites_spinner) internal lateinit var loadingSpinner: ContentLoadingProgressBar

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var unbinder: Unbinder
    private val favoritesDataManager = FavoritesDataManager.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorites, container, false)
        unbinder = ButterKnife.bind(this, rootView)
        loadingSpinner.show()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setTitle(R.string.favorites)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        favoritesAdapter = FavoritesAdapter(activity!!, this)
        favoritesGrid.layoutManager = GridLayoutManager(activity!!, 3)
        favoritesGrid.adapter = favoritesAdapter
        favoritesDataManager.registerChangeListener(this)
        favoritesDataManager.fetchFavorites(this)
    }

    override fun onFavoritesFetched(catPictures: ArrayList<CatPicture>) {
        activity!!.runOnUiThread {
            loadingSpinner.hide()
            if (catPictures.isEmpty()) {
                noResults.visibility = View.VISIBLE
            } else {
                favoritesAdapter.setPictures(catPictures)
            }
        }
    }

    override fun onFavoriteAdded(catPicture: CatPicture, changeSource: String) {
        activity!!.runOnUiThread {
            noResults.visibility = View.GONE
            favoritesAdapter.addFavorite(catPicture)
            favoritesGrid.scrollToPosition(0)
        }
    }

    override fun onFavoriteRemoved(catPicture: CatPicture, changeSource: String) {
        activity!!.runOnUiThread {
            favoritesAdapter.removeFavorite(catPicture)
            if (favoritesAdapter.itemCount == 0) {
                noResults.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(activity!!, GalleryFullViewActivity::class.java)
                .putParcelableArrayListExtra(GalleryFullViewActivity.PICTURES_KEY, favoritesAdapter.pictures)
                .putExtra(GalleryFullViewActivity.POSITION_KEY, position))
        activity!!.overridePendingTransition(R.anim.fade_in, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesDataManager.unregisterChangeListener(this)
        unbinder.unbind()
    }
}
