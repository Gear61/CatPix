package com.randomappsinc.catpix.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.randomappsinc.catpix.models.CatPicture

fun loadThumbnailImage(catPicture: CatPicture, imageView: ImageView) {
    val options = RequestOptions().centerCrop()
    Glide.with(imageView)
            .load(catPicture.getThumbnailUrlWithFallback())
            .apply(options)
            .into(imageView)
}

fun loadFullResImage(catPicture: CatPicture, imageView: ImageView, callback: RequestListener<Drawable>) {
    val options = RequestOptions().fitCenter()
    Glide.with(imageView)
            .load(catPicture.getFullResUrlWithFallback())
            .listener(callback)
            .apply(options)
            .into(imageView)
}

fun cancelImageLoading(imageView: ImageView) {
    Glide.with(imageView).clear(imageView)
}
