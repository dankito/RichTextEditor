package net.dankito.richtexteditor.android.util

import android.os.Build
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import net.dankito.richtexteditor.android.command.ToolbarCommandStyle
import net.dankito.richtexteditor.android.extensions.getLayoutSize
import net.dankito.richtexteditor.android.extensions.getPixelSizeForDisplay
import net.dankito.richtexteditor.android.extensions.setPadding


class StyleApplier {

    fun applyCommandStyle(iconResourceId: Int, style: ToolbarCommandStyle, commandView: ImageButton) {
        commandView.setImageResource(iconResourceId)
        commandView.scaleType = ImageView.ScaleType.FIT_CENTER

        commandView.setBackgroundColor(style.backgroundColor.toInt())

        val padding = commandView.getPixelSizeForDisplay(style.paddingDp)
        commandView.setPadding(padding)

        val layoutParams = commandView.layoutParams as LinearLayout.LayoutParams

        layoutParams.width = commandView.getLayoutSize(style.widthDp)
        layoutParams.height = commandView.getLayoutSize(style.heightDp)

        val rightMargin = commandView.getPixelSizeForDisplay(style.marginRightDp)
        layoutParams.rightMargin = rightMargin
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.marginEnd = rightMargin
        }
    }

}