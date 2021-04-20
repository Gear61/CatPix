package com.randomappsinc.catpix.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.randomappsinc.catpix.R

fun animateFavoriteToggle(
        favoriteToggle: TextView,
        isFavorited: Boolean,
        unfavoritedColorResId: Int,
        favoritedColorResId: Int) {
    val context = favoriteToggle.context
    val animLength = context.resources.getInteger(R.integer.shorter_anim_length)

    if (favoriteToggle.animation == null || favoriteToggle.animation.hasEnded()) {
        val animX = ObjectAnimator.ofFloat(favoriteToggle, "scaleX", 0.75f)
        val animY = ObjectAnimator.ofFloat(favoriteToggle, "scaleY", 0.75f)
        val shrink = AnimatorSet()
        shrink.playTogether(animX, animY)
        shrink.duration = animLength.toLong()
        shrink.interpolator = AccelerateInterpolator()
        shrink.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                favoriteToggle.setText(if (isFavorited) R.string.heart_filled_icon else R.string.heart_icon)
                val finalColor = ContextCompat.getColor(
                        context,
                        if (isFavorited) favoritedColorResId else unfavoritedColorResId)
                favoriteToggle.setTextColor(finalColor)

                val scaleXAnimation = ObjectAnimator.ofFloat(favoriteToggle, "scaleX", 1.0f)
                val scaleYAnimation = ObjectAnimator.ofFloat(favoriteToggle, "scaleY", 1.0f)
                val grow = AnimatorSet()
                grow.playTogether(scaleXAnimation, scaleYAnimation)
                grow.duration = animLength.toLong()
                grow.interpolator = AnticipateOvershootInterpolator()
                grow.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        shrink.start()
    }
}
