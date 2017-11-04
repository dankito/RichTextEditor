package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class SwitchTextBackgroundColorOnOffCommand: SwitchColorOnOffCommand(Color.TRANSPARENT, Color.YELLOW, Commands.BACKCOLOR, R.drawable.ic_format_color_fill_white_48dp) {

    override fun applyColor(editor: RichTextEditor, color: Int) {
        editor.setTextBackgroundColor(color)
    }

    override fun currentColorChanged(color: Int) {
        super.currentColorChanged(color)

        commandView?.setBackgroundColor(color)
    }

}