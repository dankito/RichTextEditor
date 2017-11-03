package net.dankito.richtexteditor.android.command

import android.os.Build
import android.text.Html
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.SelectValueView


abstract class SelectValueCommand(command: Commands, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : Command(command, iconResourceId, style, commandExecutedListener) {

    private var displayTexts: List<CharSequence>? = null

    private var selectValueView: SelectValueView? = null


    abstract fun initValuesDisplayTexts(): List<CharSequence>

    abstract fun valueSelected(editor: RichTextEditor, position: Int)


    override fun executeCommand(editor: RichTextEditor) {
        getSelectValueView(editor).toggleShowView()
    }

    private fun getSelectValueView(editor: RichTextEditor): SelectValueView {
        selectValueView?.let { return it }

        val view = SelectValueView(editor.context)
        view.initialize(editor, this, getValuesDisplayTexts()) { position ->
            valueSelected(editor, position)
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
        editor?.context?.getText(stringResourceId)?.toString()?.let { html ->
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

}