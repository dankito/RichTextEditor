package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.command.ToolbarCommandStyle


abstract class SelectValueCommand(command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener) {


//    private var displayTexts: List<CharSequence>? = null
//
//    private var selectValueView: SelectValueView? = null


    abstract fun getItemNames(): List<String>

    abstract fun getDefaultItemName(): String

    abstract fun getItemStyle(itemName: String): String


    fun valueSelected(itemName: String) {
        executor?.let { executor ->
            getIndexOfItem(itemName)?.let { index ->
                valueSelected(executor, index, itemName)
            }
        }
    }

    abstract fun valueSelected(executor: JavaScriptExecutorBase, position: Int, itemName: String)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
//        getSelectValueView(executor).toggleShowView()
    }


    protected fun getIndexOfItem(itemName: String): Int? {
        val index = getItemNames().indexOf(itemName)

        if(index >= 0) {
            return index
        }

        return null
    }

//    private fun getSelectValueView(executor: JavaScriptExecutorBase): SelectValueView {
//        selectValueView?.let { return it }
//
//        val unpackedEditor = editor!!
//
//        val view = SelectValueView(unpackedEditor.context)
//        view.initialize(unpackedEditor, this, getValuesDisplayTexts()) { position ->
//            valueSelected(executor, position)
//        }
//
//        this.selectValueView = view
//        return view
//    }
//
//    private fun getValuesDisplayTexts(): List<CharSequence> {
//        displayTexts?.let { return it }
//
//        val displayTexts = initValuesDisplayTexts()
//        this.displayTexts = displayTexts
//
//        return displayTexts
//    }

}