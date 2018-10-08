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

    fun requestSetWallpaperWithCurrentImage(context: Context) {
        setWallpaperListener?.onSetWallpaperRequest() ?: showLongToast(R.string.wallpaper_set_fail, context)
    }

    fun setListener(wallpaperListener: SetWallpaperListener) {
        setWallpaperListener = wallpaperListener
    }
}
