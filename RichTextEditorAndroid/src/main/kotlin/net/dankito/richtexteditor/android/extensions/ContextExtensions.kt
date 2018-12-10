package net.dankito.richtexteditor.android.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import net.dankito.utils.Color
import net.dankito.utils.android.extensions.getColorFromResource


fun Context.getRichTextEditorColor(colorResourceId: Int): Color {
    val androidColor = getColorFromResource(colorResourceId)

    return Color.fromArgb(androidColor)
}

fun Context.asActivity(): Activity? {
    if (this is Activity) {
        return this
    }
    else if (this is ContextWrapper) {
        return this.baseContext.asActivity()
    }

    // should never happen (except if called for a Service, ...)
    return null
}