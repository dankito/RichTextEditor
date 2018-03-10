package net.dankito.richtexteditor.android.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener


/**
 * Kudos go to Mirek Rusin (https://stackoverflow.com/a/12938787)
 */
class OnSwipeTouchListener(context: Context, private val swipeDetectedListener: (SwipeDirection) -> Unit) : OnTouchListener {

    enum class SwipeDirection {
        Top,
        Bottom,
        Right,
        Left
    }


    private var swipeThreshold: Int
    private var swipeVelocityThreshold: Int

    var singleTapListener: (() -> Unit)? = null
    var doubleTapListener: (() -> Unit)? = null

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())

        val displayMetrics = DisplayMetrics()
        val windowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager // the results will be higher than using the activity context or the getWindowManager() shortcut
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val rotation = windowManager.defaultDisplay.rotation
        val displayWidth = if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) displayMetrics.widthPixels else displayMetrics.heightPixels

        swipeThreshold = (displayWidth * 0.4).toInt()

        swipeVelocityThreshold = displayWidth * 5
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }


    private inner class GestureListener : SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            singleTapListener?.invoke()

            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            doubleTapListener?.invoke()

            return super.onDoubleTap(e)
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x

                if(Math.abs(diffX) > Math.abs(diffY)) {
                    if(Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocityThreshold) {
                        if(diffX > 0) {
                            onSwipeRight()
                        }
                        else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                }
                else if(Math.abs(diffY) > swipeThreshold && Math.abs(velocityY) > swipeVelocityThreshold) {
                    if(diffY > 0) {
                        onSwipeBottom()
                    }
                    else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }
    }


    private fun onSwipeRight() {
        swipeDetectedListener(SwipeDirection.Right)
    }

    private fun onSwipeLeft() {
        swipeDetectedListener(SwipeDirection.Left)
    }

    private fun onSwipeTop() {
        swipeDetectedListener(SwipeDirection.Top)
    }

    private fun onSwipeBottom() {
        swipeDetectedListener(SwipeDirection.Bottom)
    }
}