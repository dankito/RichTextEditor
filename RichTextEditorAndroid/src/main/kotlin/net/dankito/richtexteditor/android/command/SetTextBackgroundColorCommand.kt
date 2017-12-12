package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class SetTextBackgroundColorCommand(iconResourceId: Int = R.drawable.ic_format_color_fill_white_48dp, showColorInCommandView: Boolean = true)
    : SetColorCommand(Color.TRANSPARENT, showColorInCommandView, Command.BACKCOLOR, iconResourceId) {

    override fun applySelectedColor(editor: RichTextEditor, color: Int) {
        editor.setTextBackgroundColor(color)
    }

}