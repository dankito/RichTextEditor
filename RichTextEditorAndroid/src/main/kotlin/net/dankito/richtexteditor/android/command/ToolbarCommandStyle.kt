package net.dankito.richtexteditor.android.command

import android.graphics.Color
import android.view.ViewGroup


data class ToolbarCommandStyle(
        var backgroundColor: Int = DefaultBackgroundColor,
        var widthDp: Int = DefaultWidthDp,
        var heightDp: Int = DefaultHeightDp,
        var marginRightDp: Int = DefaultMarginRightDp,
        var paddingDp: Int = DefaultPaddingDp,
        var enabledTintColor: Int = DefaultEnabledTintColor,
        var disabledTintColor: Int = DefaultDisabledTintColor,
        var isActivatedColor: Int = DefaultIsActivatedColor
) {


    companion object {
        const val DefaultBackgroundColor = Color.TRANSPARENT

        const val DefaultWidthDp = 36

        const val DefaultHeightDp = ViewGroup.LayoutParams.MATCH_PARENT

        const val DefaultMarginRightDp = 1

        const val DefaultPaddingDp = 4

        const val DefaultEnabledTintColor = Color.WHITE

        val DefaultDisabledTintColor = Color.argb(97, 0, 0, 0) // see https://material.io/guidelines/style/color.html#color-usability: Dark text on light backgrounds

        const val DefaultIsActivatedColor = Color.DKGRAY

        const val GroupDefaultMarginRightDp = 12
    }

}