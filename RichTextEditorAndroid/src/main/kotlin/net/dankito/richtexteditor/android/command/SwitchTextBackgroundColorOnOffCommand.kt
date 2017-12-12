package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class SwitchTextBackgroundColorOnOffCommand(offColor: Int = Color.TRANSPARENT, onColor: Int = Color.YELLOW, iconResourceId: Int = R.drawable.ic_format_color_fill_white_48dp, showColorInCommandView: Boolean = true)
    : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, Command.BACKCOLOR, iconResourceId) {

    override fun applyColor(editor: RichTextEditor, color: Int) {
        editor.setTextBackgroundColor(color)
    }

}