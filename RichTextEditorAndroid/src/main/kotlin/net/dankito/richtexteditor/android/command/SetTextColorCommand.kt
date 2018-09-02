package net.dankito.richtexteditor.android.command

import net.dankito.utils.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.SelectColorDialog
import net.dankito.richtexteditor.command.SetTextColorCommandBase


class SetTextColorCommand(defaultColor: Color = Color.Black, icon: Icon = AndroidIcon(R.drawable.ic_format_color_text_white_48dp), showColorInCommandView: Boolean = true)
    : SetTextColorCommandBase(icon, defaultColor, showColorInCommandView), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null

    override fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit) {
        editor?.let { editor ->
            SelectColorDialog().select(currentColor, editor, colorSelected)
        }
    }

}