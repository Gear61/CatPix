package com.randomappsinc.catpix.utils

import android.graphics.drawable.Drawable
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
