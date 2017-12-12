package net.dankito.richtexteditor.android.util

import android.app.Activity


class KeyboardState {

    companion object {
        var isKeyboardVisible: Boolean = false
            private set

        fun init(activity: Activity) {
            KeyboardUtils.addKeyboardToggleListener(activity) { isVisible ->
                isKeyboardVisible = isVisible
            }
        }

    }
}