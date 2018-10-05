package com.randomappsinc.catpix.views

import android.os.Handler
import android.view.View
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.randomappsinc.catpix.R

class BottomPillViewHolder(private var viewStub: ViewStub) {

    object AnimationLengths {
        const val PILL_EXIT_DELAY_MS = 5000L
    }

    var view: View? = null
    var handler: Handler = Handler();

    fun inflateAndShow() {
        if (view == null) {
            view = viewStub.inflate()
            val context = view!!.context
            val enterFromBottom = AnimationUtils.loadAnimation(context, R.anim.enter_from_bottom)
            view!!.startAnimation(enterFromBottom)
            view!!.visibility = View.VISIBLE
            view!!.setOnClickListener(onClickListener)
            handler.postDelayed({ exit() }, AnimationLengths.PILL_EXIT_DELAY_MS)
        }
    }

    private var onClickListener: View.OnClickListener = View.OnClickListener {
        exit()
    }

    private fun exit() {
        handler.removeCallbacksAndMessages(null)
        if (view != null) {
            view!!.clearAnimation()
            view!!.setOnClickListener(null)
            val leaveThroughBottom = AnimationUtils.loadAnimation(view!!.context, R.anim.leave_through_bottom)
            leaveThroughBottom.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    view!!.visibility = View.GONE
                }

                override fun onAnimationStart(p0: Animation?) {}
            })
            view!!.startAnimation(leaveThroughBottom)
        }
    }
}
