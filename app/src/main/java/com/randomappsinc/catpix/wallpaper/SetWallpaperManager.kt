package com.randomappsinc.catpix.wallpaper

import android.content.Context
import com.randomappsinc.catpix.R
import com.randomappsinc.catpix.utils.showLongToast

class SetWallpaperManager private constructor() {

    interface SetWallpaperListener {
        fun onSetWallpaperRequest()
    }

    private object Holder { val INSTANCE = SetWallpaperManager() }

    companion object {
        val instance: SetWallpaperManager by lazy { Holder.INSTANCE }
    }

    private var setWallpaperListener: SetWallpaperListener? = null
    private var isImageLoaded = false

    fun requestSetWallpaperWithCurrentImage(context: Context) {

    }

    fun getIsImageLoaded() : Boolean {
        return isImageLoaded
    }

    fun setListener(wallpaperListener: SetWallpaperListener) {
        setWallpaperListener = wallpaperListener
    }

    fun setIsImageLoaded(isImageLoaded: Boolean) {
        this.isImageLoaded = isImageLoaded
    }
}
