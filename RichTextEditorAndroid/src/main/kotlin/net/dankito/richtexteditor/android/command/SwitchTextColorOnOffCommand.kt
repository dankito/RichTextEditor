package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor

class SwitchTextColorOnOffCommand(showColorInCommandView: Boolean = true): SwitchColorOnOffCommand(Color.BLACK, Color.RED, showColorInCommandView,
        Command.FORECOLOR, R.drawable.ic_format_color_text_white_48dp) {

    override fun applyColor(editor: RichTextEditor, color: Int) {
        editor.setTextColor(color)
    }

}