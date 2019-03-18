package net.dankito.richtexteditor.android.command

import net.dankito.utils.Color
import net.dankito.utils.image.ImageReference
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.dialogs.SelectColorDialog
import net.dankito.richtexteditor.command.SetTextBackgroundColorCommandBase


class SetTextBackgroundColorCommand(defaultColor: Color = Color.Transparent, icon: ImageReference = AndroidImageReference(R.drawable.ic_format_color_fill_white_48dp), showColorInCommandView: Boolean = true)
    : SetTextBackgroundColorCommandBase(icon, defaultColor, showColorInCommandView), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null

    override fun selectColor(currentColor: Color, colorSelected: (Color) -> Unit) {
        editor?.let { editor ->
            SelectColorDialog().select(currentColor, editor, colorSelected)
        }
    }

}