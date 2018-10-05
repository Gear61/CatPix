package com.randomappsinc.catpix.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.activities.GalleryFullViewActivity
import com.randomappsinc.catpix.adapters.HomeFeedAdapter
import com.randomappsinc.catpix.api.RestClient
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.persistence.PreferencesManager
import com.randomappsinc.catpix.persistence.database.FavoritesDataManager
import com.randomappsinc.catpix.utils.Constants
import com.randomappsinc.catpix.utils.showLongToast
import com.randomappsinc.catpix.utils.showShortToast
import com.randomappsinc.catpix.views.BottomPillViewHolder

class HomeFeedFragment : Fragment(), RestClient.Listener,
        HomeFeedAdapter.Listener, FavoritesDataManager.ChangeListener {

    fun newInstance(): HomeFeedFragment {
        val fragment = HomeFeedFragment()
        fragment.retainInstance = true
        return fragment
    }

    @BindView(R.id.skeleton_photos) lateinit var skeleton: View
    @BindView(R.id.cat_pictures_list) lateinit var catPicturesList: RecyclerView
    @BindView(R.id.bottom_pill_stub) lateinit var bottomPillStub: ViewStub

    private var feedAdapter : HomeFeedAdapter? = null
    private var restClient : RestClient = RestClient(this)
    private var fetchingNextPage = false
    private var pageToFetch = 0
    private lateinit var preferencesManager : PreferencesManager
    private var unbinder: Unbinder? = null
    private var favoritesDataManager = FavoritesDataManager.instance
    private lateinit var bottomPillViewHolder: BottomPillViewHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.home_feed, container, false)
        unbinder = ButterKnife.bind(this, rootView)
        bottomPillViewHolder = BottomPillViewHolder(bottomPillStub)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesManager = PreferencesManager(activity!!)
        feedAdapter = HomeFeedAdapter(activity!!, this)
        catPicturesList.adapter = feedAdapter
        val layoutManager = GridLayoutManager(activity!!, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (feedAdapter!!.isPositionASpinner(position)) layoutManager.spanCount else 1
            }
        }
        catPicturesList.layoutManager = layoutManager

        pageToFetch = preferencesManager.nextPageToFetch
        restClient.fetchPictures(pageToFetch)

        favoritesDataManager.registerChangeListener(this)
    }

    override fun onPicturesFetched(pictures: ArrayList<CatPicture>) {
        if (preferencesManager.shouldShowTapInstructions()) {
            preferencesManager.rememberTapInstructionsSeen()
            bottomPillViewHolder.inflateAndShow()
        }
        if (pictures.size < Constants.EXPECTED_PAGE_SIZE) {
            pageToFetch = 0
        } else {
            pageToFetch++
        }
        preferencesManager.nextPageToFetch = pageToFetch
        skeleton.visibility = View.GONE
        feedAdapter!!.addPicturesUrls(pictures)
        fetchingNextPage = false
        catPicturesList.visibility = View.VISIBLE
    }

    override fun onPictureFetchFail() {
        showLongToast(R.string.failed_to_fetch_cats, context)
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(activity!!, GalleryFullViewActivity::class.java)
                .putParcelableArrayListExtra(GalleryFullViewActivity.PICTURES_KEY, feedAdapter!!.pictures)
                .putExtra(GalleryFullViewActivity.POSITION_KEY, position))
        activity!!.overridePendingTransition(R.anim.fade_in, 0)
    }

    override fun onItemDoubleTap(catPicture: CatPicture) {
        if (favoritesDataManager.isPictureFavorited(catPicture)) {
            favoritesDataManager.removeFavorite(catPicture, Constants.HOME_FEED)
            showShortToast(R.string.removed_from_favorites, context)
        } else {
            favoritesDataManager.addFavorite(catPicture, Constants.HOME_FEED)
            showShortToast(R.string.added_to_favorites, context)
        }
    }

    override fun onLastItemSeen() {
        if (!fetchingNextPage) {
            fetchingNextPage = true
            restClient.fetchPictures(pageToFetch)
        }
    }

    override fun onFavoriteAdded(catPicture: CatPicture, changeSource: String) {
        if (changeSource != Constants.HOME_FEED) {
            feedAdapter?.onFavoriteStatusChanged(catPicture)
        }
    }

    override fun onFavoriteRemoved(catPicture: CatPicture, changeSource: String) {
        if (changeSource != Constants.HOME_FEED) {
            feedAdapter?.onFavoriteStatusChanged(catPicture)
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        activity!!.overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesDataManager.unregisterChangeListener(this)
        unbinder!!.unbind()
    }
}
