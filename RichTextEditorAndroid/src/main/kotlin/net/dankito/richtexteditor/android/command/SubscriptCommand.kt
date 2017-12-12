package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor

class SubscriptCommand(iconResourceId: Int = R.drawable.ic_format_subscript) : ActiveStateToolbarCommand(Command.SUBSCRIPT, iconResourceId) {

    override fun executeCommand(editor: RichTextEditor) {
        editor.setSubscript()
    }

}