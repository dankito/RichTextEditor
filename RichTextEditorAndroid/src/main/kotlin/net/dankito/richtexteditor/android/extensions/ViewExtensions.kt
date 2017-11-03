package net.dankito.richtexteditor.android.extensions

import android.content.Context
import android.view.MotionEvent
import android.view.View
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