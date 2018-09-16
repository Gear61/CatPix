package com.randomappsinc.catpix.views

import android.content.Context
import android.util.AttributeSet

class SquareSkeletonView(context: Context, attrs: AttributeSet) : SkeletonView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
