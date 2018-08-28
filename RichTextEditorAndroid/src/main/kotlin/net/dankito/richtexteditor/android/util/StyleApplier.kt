package net.dankito.richtexteditor.android.util

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.toolbar.SelectValueWithPreviewView
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.utils.extensions.getLayoutSize
import net.dankito.utils.extensions.getPixelSizeForDisplay
import net.dankito.utils.extensions.setPadding
import net.dankito.utils.extensions.setRightMargin


class StyleApplier {

    fun applyCommandStyle(icon: Icon, style: ToolbarCommandStyle, commandView: View) {
        (icon as? AndroidIcon)?.let {
            if(commandView is ImageView) {
                commandView.setImageResource(it.iconResourceId)
                commandView.scaleType = ImageView.ScaleType.FIT_CENTER
            }
            else if(commandView is SelectValueWithPreviewView) {
                commandView.icon.setImageResource(it.iconResourceId)
                commandView.icon.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }

        commandView.setBackgroundColor(style.backgroundColor.toInt())

        val padding = commandView.getPixelSizeForDisplay(style.paddingDp)
        if(commandView is SelectValueWithPreviewView) {
            commandView.icon.setPadding(padding)
        }
        else {
            commandView.setPadding(padding)
        }

        val layoutParams = commandView.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.height = commandView.getLayoutSize(style.heightDp)
        layoutParams.width = commandView.getLayoutSize(style.widthDp)

        if(commandView is SelectValueWithPreviewView) {
            applySelectValueWithPreviewViewStyle(commandView, style, layoutParams)
        }

        val rightMargin = commandView.getPixelSizeForDisplay(style.marginRightDp)
        layoutParams.setRightMargin(rightMargin)
    }

    fun applySelectValueWithPreviewViewStyle(commandView: SelectValueWithPreviewView, style: ToolbarCommandStyle, layoutParams: ViewGroup.MarginLayoutParams) {
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT

        (commandView.icon.layoutParams as? ViewGroup.MarginLayoutParams)?.let { iconParams ->
            iconParams.width = commandView.icon.getLayoutSize(style.widthDp)
        }
    }

}