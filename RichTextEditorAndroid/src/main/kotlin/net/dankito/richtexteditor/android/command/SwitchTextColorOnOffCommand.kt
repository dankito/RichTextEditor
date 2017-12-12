package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor

class SwitchTextColorOnOffCommand(offColor: Int = Color.BLACK, onColor: Int = Color.RED, iconResourceId: Int = R.drawable.ic_format_color_text_white_48dp,
                                  showColorInCommandView: Boolean = true) : SwitchColorOnOffCommand(offColor, onColor, showColorInCommandView, Command.FORECOLOR, iconResourceId) {

    override fun applyColor(editor: RichTextEditor, color: Int) {
        editor.setTextColor(color)
    }

}