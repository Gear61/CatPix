package com.randomappsinc.catpix.persistence.database

public class FavoritesDataManager private constructor() {

    init { println("This ($this) is a singleton") }

    private object Holder { val INSTANCE = FavoritesDataManager() }

    companion object {
        val instance: FavoritesDataManager by lazy { Holder.INSTANCE }
    }

    var b:String? = null
}
