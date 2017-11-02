package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.RichTextEditor


abstract class Command(val command: Commands,
                       val iconResourceId: Int,
                       val style: ToolbarCommandStyle = ToolbarCommandStyle(),
                       val commandExecutedListener: (() -> Unit)? = null) {


    var editor: RichTextEditor? = null


    fun commandInvoked() {
        editor?.let {
            executeCommand(it)
        }

        commandExecutedListener?.invoke()
    }

    abstract protected fun executeCommand(editor: RichTextEditor)

}