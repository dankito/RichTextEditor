package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor

class DecreaseIndentCommand : ToolbarCommand(Commands.OUTDENT, R.drawable.ic_format_indent_decrease_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setOutdent()
    }

}