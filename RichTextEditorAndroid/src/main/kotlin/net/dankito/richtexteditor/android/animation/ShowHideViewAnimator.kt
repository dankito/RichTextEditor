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
        playVerticalAnimation(view, true, yStart, yEnd, animationDurationMillis)
    }

    fun playHideAnimation(view: View, yStart: Float = view.top.toFloat(), yEnd: Float = -1 * view.measuredHeight.toFloat(), animationDurationMillis: Long = DefaultAnimationDurationMillis) {
        playVerticalAnimation(view, false, yStart, yEnd, animationDurationMillis) {
            view.visibility = View.GONE
        }
    }

    fun playVerticalAnimation(view: View, show: Boolean, yStart: Float, yEnd: Float, animationDurationMillis: Long = DefaultAnimationDurationMillis, animationEndListener: (() -> Unit)? = null) {
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
                if(show == false) {
                    view.visibility = View.GONE
                }

                animationEndListener?.invoke()
            }

        })

        animatorSet.playTogether(yAnimator, alphaAnimator)
        animatorSet.start()
    }


    fun playHorizontalAnimation(view: View, show: Boolean, xStart: Float, xEnd: Float, animationDurationMillis: Long = DefaultAnimationDurationMillis, animationEndListener: (() -> Unit)? = null) {
        val xAnimator = ObjectAnimator
                .ofFloat(view, View.X, xStart, xEnd)
                .setDuration(animationDurationMillis)
        xAnimator.interpolator = if(show) AccelerateInterpolator() else DecelerateInterpolator()

        val alphaAnimator = ObjectAnimator
                .ofFloat(view, View.ALPHA, if(show) 0f else 1f, if(show) 1f else 0f)
                .setDuration(animationDurationMillis)
        alphaAnimator.interpolator = if(show) AccelerateInterpolator() else DecelerateInterpolator()

        val animatorSet = AnimatorSet()

        xAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationRepeat(animation: Animator) { }

            override fun onAnimationCancel(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                if(show == false) {
                    view.visibility = View.GONE
                }

                animationEndListener?.invoke()
            }

        })

        animatorSet.playTogether(xAnimator, alphaAnimator)
        animatorSet.start()
    }

}