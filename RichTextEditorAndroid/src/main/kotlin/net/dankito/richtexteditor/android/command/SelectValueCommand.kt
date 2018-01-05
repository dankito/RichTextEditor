package net.dankito.richtexteditor.android.command

import android.os.Build
import android.text.Html
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.SelectValueView
import net.dankito.richtexteditor.command.Command


abstract class SelectValueCommand(command: Command, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ToolbarCommand(command, icon, style, commandExecutedListener), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null


    private var displayTexts: List<CharSequence>? = null

    private var selectValueView: SelectValueView? = null


    abstract fun initValuesDisplayTexts(): List<CharSequence>

    abstract fun valueSelected(executor: JavaScriptExecutorBase, position: Int)


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        getSelectValueView(executor).toggleShowView()
    }

    private fun getSelectValueView(executor: JavaScriptExecutorBase): SelectValueView {
        selectValueView?.let { return it }

        val unpackedEditor = editor!!

        val view = SelectValueView(unpackedEditor.context)
        view.initialize(unpackedEditor, this, getValuesDisplayTexts()) { position ->
            valueSelected(executor, position)
        }

        this.selectValueView = view
        return view
    }

    private fun getValuesDisplayTexts(): List<CharSequence> {
        displayTexts?.let { return it }

        val displayTexts = initValuesDisplayTexts()
        this.displayTexts = displayTexts

        return displayTexts
    }

    protected fun getHtmlSpanned(stringResourceId: Int): CharSequence {
        selectValueView?.context?.getText(stringResourceId)?.toString()?.let { html ->
            return getHtmlSpanned(html)
        }

        return ""
    }

    protected fun getHtmlSpanned(html: String): CharSequence {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).trim() // Html.fromHtml() sometimes adds new lines add the end
        }
        else {
            return Html.fromHtml(html).trim() // Html.fromHtml() sometimes adds new lines add the end
        }
    }


    fun handlesBackButtonPress(): Boolean {
        selectValueView?.let {
            return it.handlesBackButtonPress()
        }

        return false
    }

}