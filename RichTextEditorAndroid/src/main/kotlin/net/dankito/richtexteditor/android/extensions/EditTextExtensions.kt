package net.dankito.richtexteditor.android.extensions

import android.widget.EditText
import android.widget.TextView


fun EditText.clearCaretColor() {
    // textCursorDrawable is not settable from code, but we have to set it to null in order that caret color takes text color
    try {
        // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
        val cursorDrawableField = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        cursorDrawableField.isAccessible = true
        cursorDrawableField.set(this, 0)
    } catch (ignored: Exception) { }
}