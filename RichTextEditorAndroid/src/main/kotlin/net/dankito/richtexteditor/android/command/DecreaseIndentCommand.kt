package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor

class DecreaseIndentCommand(iconResourceId: Int = R.drawable.ic_format_indent_decrease_white_48dp) : ToolbarCommand(Command.OUTDENT, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setOutdent()
    }

}