package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.SelectColorDialog
import net.dankito.richtexteditor.command.SetTextBackgroundColorCommandBase


class SetTextBackgroundColorCommand(defaultColor: Color = Color.Transparent, icon: Icon = AndroidIcon(R.drawable.ic_format_color_fill_white_48dp), showColorInCommandView: Boolean = true)
    : SetTextBackgroundColorCommandBase(icon, defaultColor, showColorInCommandView), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null

    override fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit) {
        editor?.let { editor ->
            SelectColorDialog().select(currentColor, editor, colorSelected)
        }
    }

}