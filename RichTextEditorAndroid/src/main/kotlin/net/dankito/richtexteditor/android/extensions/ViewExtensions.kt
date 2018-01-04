package net.dankito.richtexteditor.android.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager


fun View.showKeyboard() {
    this.context?.let { context ->
        this.requestFocus()

        val keyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(this, 0)
    }
}

fun View.showKeyboardDelayed(delayMillis: Long = 50L) {
    this.postDelayed({
        this.showKeyboard()
    }, delayMillis)
}

fun View.hideKeyboard(flags: Int = 0) {
    this.context?.let { context ->
        val keyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(this.windowToken, flags)
    }
}


fun View.getPixelSizeForDisplay(deviceIndependentPixel: Int): Int {
    return (deviceIndependentPixel * getDisplayDensity()).toInt()
}

fun View.getLayoutSize(sizeInDp: Int): Int {
    if(sizeInDp >= 0) {
        return getPixelSizeForDisplay(sizeInDp)
    }
    else { // e.g. ViewGroup.LayoutParams.MATCH_PARENT
        return sizeInDp
    }
}

fun View.getDisplayDensity(): Float {
    return this.context.resources.displayMetrics.density
}


fun View.setPadding(paddingTopBottomLeftRight: Int) {
    this.setPadding(paddingTopBottomLeftRight, paddingTopBottomLeftRight, paddingTopBottomLeftRight, paddingTopBottomLeftRight)
}


fun View.setBackgroundTintColor(color: Int) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList = ColorStateList.valueOf(color)
    }
    else {
        ViewCompat.setBackgroundTintList(this, ContextCompat.getColorStateList(this.context, color))
    }
}


fun View.executeActionAfterMeasuringSize(forceWaitForMeasuring: Boolean = false, action: () -> Unit) {
    if(this.measuredHeight == 0 || forceWaitForMeasuring) { // in this case we have to wait till height is determined -> set OnGlobalLayoutListener
        var layoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null // have to do it that complicated otherwise in OnGlobalLayoutListener we cannot access layoutListener variable
        layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            removeOnGlobalLayoutListener(layoutListener)

            action()
        }

        this.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }
    else {
        action()
    }
}

fun View.removeOnGlobalLayoutListener(layoutListener: ViewTreeObserver.OnGlobalLayoutListener?) {
    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
        this.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }
    else {
        this.viewTreeObserver.removeGlobalOnLayoutListener(layoutListener)
    }
}