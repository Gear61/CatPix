package com.randomappsinc.catpix.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.randomappsinc.catpix.models.CatPicture

fun loadThumbnailImage(catPicture: CatPicture, imageView: ImageView, placeholder: Drawable) {
    val options = RequestOptions()
            .centerCrop()
            .placeholder(placeholder)
    Glide.with(imageView)
            .load(catPicture.getThumbnailUrlWithFallback())
            .transition(withCrossFade())
            .apply(options)
            .into(imageView)
}

fun loadFullResImage(catPicture: CatPicture, imageView: ImageView, callback: RequestListener<Drawable>) {
    val options = RequestOptions().fitCenter()
    val isGif = catPicture.getFullResUrlWithFallback()!!.endsWith(".gif")
    val sameUrls = catPicture.getThumbnailUrlWithFallback().equals(catPicture.getFullResUrlWithFallback())
    if (isGif || !sameUrls) {
        Glide.with(imageView)
                .load(catPicture.getFullResUrlWithFallback())
                .transition(withCrossFade())
                .listener(callback)
                .apply(options)
                .into(imageView)
    } else {
        Glide.with(imageView)
                .load(catPicture.getFullResUrlWithFallback())
                .transition(withCrossFade())
                .thumbnail(Glide.with(imageView).load(catPicture.getThumbnailUrlWithFallback()))
                .listener(callback)
                .apply(options)
                .into(imageView)
    }
}

fun cancelImageLoading(imageView: ImageView) {
    Glide.with(imageView).clear(imageView)
}

fun getFullscreenBitmapFromDrawable(drawable: Drawable, context: Context): Bitmap {
    val screenWidth = context.resources.displayMetrics.widthPixels
    val screenHeight = context.resources.displayMetrics.heightPixels
    val imageWidth = drawable.intrinsicWidth
    val imageHeight = drawable.intrinsicHeight

    var finalWidth = screenWidth
    var finalHeight = screenHeight
    if (imageWidth > imageHeight) {
        finalHeight = (screenHeight * imageHeight) / imageWidth
    } else {
        finalWidth = (screenWidth * imageWidth) / imageHeight
    }
    val mutableBitmap = Bitmap.createBitmap(
            finalWidth,
            finalHeight,
            Bitmap.Config.ARGB_8888)
    val canvas = Canvas(mutableBitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return mutableBitmap
}

fun getScreenShot(view: View): Bitmap {
    val screenView = view.rootView
    screenView.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(screenView.drawingCache)
    screenView.isDrawingCacheEnabled = false
    return bitmap
}
