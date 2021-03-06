package com.randomappsinc.catpix.persistence.database

import android.content.Context
import com.randomappsinc.catpix.models.CatPicture

class FavoritesDataManager private constructor() : FavoritesDataSource.FavoritesFetcher {

    interface ChangeListener {
        fun onFavoriteAdded(catPicture: CatPicture, changeSource: String)

        fun onFavoriteRemoved(catPicture: CatPicture, changeSource: String)
    }

    interface FavoritesReceiver {
        fun onFavoritesFetched(catPictures: ArrayList<CatPicture>)
    }

    private object Holder { val INSTANCE = FavoritesDataManager() }

    companion object {
        val instance: FavoritesDataManager by lazy { Holder.INSTANCE }
    }

    private var dataSource: FavoritesDataSource? = null
    private var changeListeners = HashSet<ChangeListener>()
    var favoriteIds = HashSet<String>()

    fun initialize(context: Context) {
        dataSource = FavoritesDataSource(context)
        dataSource!!.fetchFavorites(this)
    }

    fun addFavorite(catPicture: CatPicture, changeSource: String) {
        favoriteIds.add(catPicture.id)
        dataSource!!.addFavorite(catPicture)
        for (listener in changeListeners) {
            listener.onFavoriteAdded(catPicture, changeSource)
        }
    }

    fun removeFavorite(catPicture: CatPicture, changeSource: String) {
        favoriteIds.remove(catPicture.id)
        dataSource!!.removeFavorite(catPicture)
        for (listener in changeListeners) {
            listener.onFavoriteRemoved(catPicture, changeSource)
        }
    }

    fun fetchFavorites(favoritesReceiver: FavoritesReceiver) {
        dataSource!!.fetchFavorites(object : FavoritesDataSource.FavoritesFetcher {
            override fun onFavoritesFetched(catPictures: ArrayList<CatPicture>) {
                favoritesReceiver.onFavoritesFetched(catPictures)
            }
        })
    }

    fun registerChangeListener(changeListener: ChangeListener) {
        changeListeners.add(changeListener)
    }

    fun unregisterChangeListener(changeListener: ChangeListener) {
        changeListeners.remove(changeListener)
    }

    fun isPictureFavorited(catPicture: CatPicture): Boolean {
        return favoriteIds.contains(catPicture.id)
    }

    // Called when the DB returns us a list of favorites
    // Here, we set up our favorited IDs HashSet to give favorite status in O(1) time
    override fun onFavoritesFetched(catPictures: ArrayList<CatPicture>) {
        for (catPicture in catPictures) {
            favoriteIds.add(catPicture.id)
        }
    }
}
