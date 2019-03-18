package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand


class EnterViewingModeCommand : ToolbarCommand(CommandName.ENTER_VIEWING_MODE, AndroidImageReference(R.drawable.ic_fullscreen_white_48dp)), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        editor?.enterViewingMode()
    }

}