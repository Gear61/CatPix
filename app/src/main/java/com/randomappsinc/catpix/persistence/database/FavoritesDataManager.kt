package com.randomappsinc.catpix.persistence.database

import android.content.Context
import com.randomappsinc.catpix.models.CatPicture

class FavoritesDataManager private constructor() {

    interface Listener {
        fun onFavoriteAdded(catPicture: CatPicture)

        fun onFavoriteRemoved(catPicture: CatPicture)
    }

    init {}

    private object Holder { val INSTANCE = FavoritesDataManager() }

    companion object {
        val instance: FavoritesDataManager by lazy { Holder.INSTANCE }
    }

    var dataSource: FavoritesDataSource? = null
    var listeners = HashSet<Listener>()

    fun initialize(context: Context) {
        dataSource = FavoritesDataSource(context)
    }

    fun addFavorite(catPicture: CatPicture) {
        for (listener in listeners) {
            listener.onFavoriteAdded(catPicture)
        }
        dataSource!!.addFavorite(catPicture)
    }

    fun removeFavorite(catPicture: CatPicture) {
        for (listener in listeners) {
            listener.onFavoriteRemoved(catPicture)
        }
        dataSource!!.removeFavorite(catPicture)
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun isPictureFavorited(catPicture: CatPicture): Boolean {
        return dataSource!!.isPictureFavorited(catPicture)
    }
}
