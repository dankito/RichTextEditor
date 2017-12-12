package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class SwitchTextBackgroundColorOnOffCommand(iconResourceId: Int = R.drawable.ic_format_color_fill_white_48dp, showColorInCommandView: Boolean = true)
    : SwitchColorOnOffCommand(Color.TRANSPARENT, Color.YELLOW, showColorInCommandView, Command.BACKCOLOR, iconResourceId) {

    override fun applyColor(editor: RichTextEditor, color: Int) {
        editor.setTextBackgroundColor(color)
    }

}