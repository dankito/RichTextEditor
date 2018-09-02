package net.dankito.richtexteditor.command

import net.dankito.utils.Color


data class ToolbarCommandStyle(
        var backgroundColor: Color = DefaultBackgroundColor,
        var widthDp: Int = DefaultWidthDp,
        var heightDp: Int = DefaultHeightDp,
        var marginRightDp: Int = DefaultMarginRightDp,
        var paddingDp: Int = DefaultPaddingDp,
        var enabledTintColor: Color = DefaultEnabledTintColor,
        var disabledTintColor: Color = DefaultDisabledTintColor,
        var isActivatedColor: Color = DefaultIsActivatedColor
) {


    companion object {
        val DefaultBackgroundColor = Color.Transparent

        const val DefaultWidthDp = 36

        const val DefaultHeightDp = -1 // -1 for ViewGroup.LayoutParams.MATCH_PARENT

        const val DefaultMarginRightDp = 1

        const val DefaultPaddingDp = 4

        val DefaultEnabledTintColor = Color.White

        val DefaultDisabledTintColor = Color(0, 0, 0, 97) // see https://material.io/guidelines/style/color.html#color-usability: Dark text on light backgrounds

        val DefaultIsActivatedColor = Color.DarkGray

        const val GroupDefaultMarginRightDp = 12
    }

}