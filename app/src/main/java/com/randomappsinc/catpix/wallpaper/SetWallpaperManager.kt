package com.randomappsinc.catpix.wallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import com.randomappsinc.catpix.utils.getScreenShot
import java.io.IOException

class SetWallpaperManager private constructor() {

    interface SetWallpaperListener {
        fun onSetWallpaperSuccess()

        fun onSetWallpaperFail()
    }

    private object Holder { val INSTANCE = SetWallpaperManager() }

    companion object {
        val instance: SetWallpaperManager by lazy { Holder.INSTANCE }
    }

    private var setWallpaperListener: SetWallpaperListener? = null
    private var isImageLoaded = false
    private val backgroundHandler: Handler
    private val uiHandler: Handler

    init {
        val handlerThread = HandlerThread("Wallpaper Manager")
        handlerThread.start()
        backgroundHandler = Handler(handlerThread.looper)
        uiHandler = Handler(Looper.getMainLooper())
    }

    /**
     * We have a variety of images with different aspect ratios and formats (gif and non-gif),
     * so we hack the wallpaper setting by taking a screenshot of the current screen and using that.
     */
    fun setWallpaper(activity: Activity, rootId: Int) {
        backgroundHandler.post {
            val rootView = activity.window.decorView.findViewById<View>(rootId)
            val bitmap = getScreenShot(rootView)
            try {
                val wallpaperManager = WallpaperManager.getInstance(activity)
                wallpaperManager.setBitmap(bitmap)
                alertListenerOfSuccess()
            } catch (e: IOException) {
                alertListenerOfFailure()
            }
        }
    }

    fun alertListenerOfSuccess() {
        uiHandler.post {
            setWallpaperListener?.onSetWallpaperSuccess()
        }
    }

    fun alertListenerOfFailure() {
        uiHandler.post {
            setWallpaperListener?.onSetWallpaperFail()
        }
    }

    fun setListener(wallpaperListener: SetWallpaperListener) {
        setWallpaperListener = wallpaperListener
    }

    fun clearListener() {
        setWallpaperListener = null
    }

    fun isImageLoaded() : Boolean {
        return isImageLoaded
    }

    fun setIsImageLoaded(isImageLoaded: Boolean) {
        this.isImageLoaded = isImageLoaded
    }
}
