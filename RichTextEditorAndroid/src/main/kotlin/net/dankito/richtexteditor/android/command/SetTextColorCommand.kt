package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class SetTextColorCommand: SetColorCommand(Color.BLACK, Command.FORECOLOR, R.drawable.ic_format_color_text_white_48dp) {

    override fun applySelectedColor(editor: RichTextEditor, color: Int) {
        editor.setTextColor(color)
    }

}