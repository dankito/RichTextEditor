package net.dankito.richtexteditor.android.extensions

import android.content.Context
import net.dankito.richtexteditor.Color
import net.dankito.utils.extensions.getColorFromResource


fun Context.getRichTextEditorColor(colorResourceId: Int): Color {
    val androidColor = getColorFromResource(colorResourceId)

    return Color.fromArgb(androidColor)
}