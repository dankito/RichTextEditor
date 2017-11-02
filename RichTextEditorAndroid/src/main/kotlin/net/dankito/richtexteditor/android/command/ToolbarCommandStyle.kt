package net.dankito.richtexteditor.android.command

import android.graphics.Color
import android.view.ViewGroup


data class ToolbarCommandStyle(
        val backgroundColorResourceId: Int = android.R.color.transparent,
        val widthDp: Int = DefaultWidthDp,
        val heightDp: Int = ViewGroup.LayoutParams.MATCH_PARENT,
        val marginRightDp: Int = DefaultMarginRightDp,
        val paddingDp: Int = DefaultPaddingDp,
        val enabledTintColor: Int = Color.WHITE,
        val disabledTintColor: Int = Color.argb(97, 0, 0, 0) // see https://material.io/guidelines/style/color.html#color-usability: Dark text on light backgrounds
) {


    companion object {
        private const val DefaultWidthDp = 36

        private const val DefaultMarginRightDp = 4

        private const val DefaultPaddingDp = 4
    }

}