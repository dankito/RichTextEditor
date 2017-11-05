package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class IncreaseIndentCommand : ToolbarCommand(Command.INDENT, R.drawable.ic_format_indent_increase_white_48dp) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setIndent()
    }

}