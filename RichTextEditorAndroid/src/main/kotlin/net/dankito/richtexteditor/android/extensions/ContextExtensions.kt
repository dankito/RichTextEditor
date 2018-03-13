package net.dankito.richtexteditor.android.extensions

import android.content.Context
import android.support.v4.content.ContextCompat
import net.dankito.richtexteditor.Color


fun Context.getColorFromResource(colorResourceId: Int): Int {
    return ContextCompat.getColor(this, colorResourceId)
}

fun Context.getRichTextEditorColor(colorResourceId: Int): Color {
    val androidColor = getColorFromResource(colorResourceId)

    return Color.fromArgb(androidColor)
}