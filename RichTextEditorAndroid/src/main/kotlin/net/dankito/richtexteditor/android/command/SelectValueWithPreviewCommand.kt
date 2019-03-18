package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.CommandView
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidCommandView
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.SelectValueWithPreviewView
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommandStyle


abstract class SelectValueWithPreviewCommand(command: CommandName, icon: ImageReference, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : SelectValueCommandBase(command, icon, style, commandExecutedListener) {

    override var editor: RichTextEditor? = null
        set(value) {
            field = value

            ((this.commandView as? AndroidCommandView)?.view as? SelectValueWithPreviewView)?.let { view ->
                value?.let { editor ->
                    view.initialize(editor, this, getValuesDisplayTexts()) { position ->
                        executor?.let { executor ->
                            valueSelected(executor, position)
                        }
                    }
                }
            }
        }

    abstract fun getPreviewTextForCommandValue(commandValue: String): CharSequence

    abstract fun getDefaultPreview(): CharSequence


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        getSelectValueWithPreviewView()?.selectValue()
    }

    protected open fun getSelectValueWithPreviewView(): SelectValueWithPreviewView? {
        return (commandView as? AndroidCommandView)?.view as? SelectValueWithPreviewView
    }


    override fun commandValueChanged(commandView: CommandView, commandValue: Any) {
        super.commandValueChanged(commandView, commandValue)

        if(commandValue is String) {
            getSelectValueWithPreviewView()?.setPreviewText(getPreviewTextForCommandValue(commandValue))
        }
    }

    override fun setIconTintColorToExecutableState(commandView: CommandView, isExecutable: Boolean) {
        if(isExecutable) {
            commandView.setTintColor(style.enabledTintColor)
        }
        else {
            commandView.setTintColor(style.disabledTintColor)
        }
    }

}