package com.randomappsinc.catpix.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.randomappsinc.catpix.R

open class SkeletonView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val colorAnimator: ValueAnimator

    init {
        val from = FloatArray(3)
        val to = FloatArray(3)

        Color.colorToHSV(ContextCompat.getColor(context, R.color.gray_100), from)
        Color.colorToHSV(ContextCompat.getColor(context, R.color.gray_300), to)

        colorAnimator = ValueAnimator.ofFloat(0f, 1f)
        colorAnimator.duration = context.resources.getInteger(R.integer.skeleton_anim_length).toLong()
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE

        val hsv = FloatArray(3)
        colorAnimator.addUpdateListener { animation ->
            // Transition along each axis of HSV (hue, saturation, value)
            hsv[0] = from[0] + (to[0] - from[0]) * animation.animatedFraction
            hsv[1] = from[1] + (to[1] - from[1]) * animation.animatedFraction
            hsv[2] = from[2] + (to[2] - from[2]) * animation.animatedFraction

            setBackgroundColor(Color.HSVToColor(hsv))
        }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        colorAnimator.start()
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        colorAnimator.cancel()
    }
}
