package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.command.ToolbarCommandStyle


abstract class SelectValueCommand(command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener) {


    abstract fun getItemNames(): List<String>

    abstract fun getDefaultItemName(): String

    abstract fun getItemStyle(itemName: String): String

    abstract fun valueSelected(executor: JavaScriptExecutorBase, position: Int, itemName: String)


    fun valueSelected(itemName: String) {
        executor?.let { executor ->
            getIndexOfItem(itemName)?.let { index ->
                valueSelected(executor, index, itemName)
            }
        }
    }


    override fun executeCommand(executor: JavaScriptExecutorBase) {

    }


    protected fun getIndexOfItem(itemName: String): Int? {
        val index = getItemNames().indexOf(itemName)

        if(index >= 0) {
            return index
        }

        return null
    }

}