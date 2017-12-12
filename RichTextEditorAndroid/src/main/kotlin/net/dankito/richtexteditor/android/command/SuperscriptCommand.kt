package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor

class SuperscriptCommand(iconResourceId: Int = R.drawable.ic_format_superscript) : ActiveStateToolbarCommand(Command.SUPERSCRIPT, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setSuperscript()
    }

}