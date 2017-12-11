package net.dankito.richtexteditor.android.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator


class ShowHideViewAnimator {

    companion object {
        private const val DefaultAnimationDurationMillis = 250L
    }


    fun playShowAnimation(view: View, yStart: Float = -1 * view.measuredHeight.toFloat(), yEnd: Float =  view.top.toFloat(), animationDurationMillis: Long = DefaultAnimationDurationMillis) {
        view.visibility = View.VISIBLE
        playAnimation(view, true, yStart, yEnd, animationDurationMillis)
    }

    fun playHideAnimation(view: View, yStart: Float = view.bottom.toFloat(), yEnd: Float = view.top.toFloat(), animationDurationMillis: Long = DefaultAnimationDurationMillis) {
        playAnimation(view, false, yStart, yEnd, animationDurationMillis) {
            view.visibility = View.GONE
        }
    }

    fun playAnimation(view: View, show: Boolean, yStart: Float, yEnd: Float, animationDurationMillis: Long = DefaultAnimationDurationMillis, animationEndListener: (() -> Unit)? = null) {
        val yAnimator = ObjectAnimator
                .ofFloat(view, View.Y, yStart, yEnd)
                .setDuration(animationDurationMillis)
        yAnimator.interpolator = if(show) AccelerateInterpolator() else DecelerateInterpolator()

        val alphaAnimator = ObjectAnimator
                .ofFloat(view, View.ALPHA, if(show) 0f else 1f, if(show) 1f else 0f)
                .setDuration(animationDurationMillis)
        alphaAnimator.interpolator = if(show) AccelerateInterpolator() else DecelerateInterpolator()

        val animatorSet = AnimatorSet()

        yAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationRepeat(animation: Animator) { }

            override fun onAnimationCancel(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                animationEndListener?.invoke()
            }

        })

        animatorSet.playTogether(yAnimator, alphaAnimator)
        animatorSet.start()
    }

}