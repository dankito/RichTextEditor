package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.command.ToolbarCommandStyle


abstract class SelectValueCommandBase(command: CommandName, icon: ImageReference, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null


    protected open var displayTexts: List<CharSequence>? = null


    abstract fun initValuesDisplayTexts(): List<CharSequence>

    abstract fun valueSelected(executor: JavaScriptExecutorBase, position: Int)


    protected open fun getValuesDisplayTexts(): List<CharSequence> {
        displayTexts?.let { return it }

        val displayTexts = initValuesDisplayTexts()
        this.displayTexts = displayTexts

        return displayTexts
    }

}