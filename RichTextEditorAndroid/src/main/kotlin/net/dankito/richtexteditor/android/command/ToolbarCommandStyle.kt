package net.dankito.richtexteditor.android.command

import android.view.ViewGroup


data class ToolbarCommandStyle(
        val backgroundColorResourceId: Int = android.R.color.transparent,
        val widthDp: Int = DefaultWidthDp,
        val heightDp: Int = ViewGroup.LayoutParams.MATCH_PARENT,
        val marginRightDp: Int = DefaultMarginRightDp,
        val paddingDp: Int = DefaultPaddingDp
) {


    companion object {
        private const val DefaultWidthDp = 36

        private const val DefaultMarginRightDp = 4

        private const val DefaultPaddingDp = 4
    }

}