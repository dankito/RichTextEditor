package net.dankito.richtexteditor.android.util

import android.app.Activity


class KeyboardState {

    companion object {

        var isKeyboardVisible: Boolean = false
            private set

        private val keyboardToggleListener = KeyboardUtils.SoftKeyboardToggleListener { isVisible ->
            isKeyboardVisible = isVisible
        }


        fun init(activity: Activity) {
            KeyboardUtils.addKeyboardToggleListener(activity, keyboardToggleListener)
        }

        /**
         * It's very important to call cleanUp() when done to avoid memory leaks!
         */
        fun cleanUp() {
            KeyboardUtils.removeKeyboardToggleListener(keyboardToggleListener)
        }

    }

}