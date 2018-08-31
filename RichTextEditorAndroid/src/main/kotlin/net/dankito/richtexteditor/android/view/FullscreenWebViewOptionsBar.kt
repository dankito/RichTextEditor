package net.dankito.richtexteditor.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_fullscreen_options_bar.view.*
import net.dankito.richtexteditor.android.AndroidCommandView
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.TextMarkerCommand
import net.dankito.richtexteditor.android.util.StyleApplier


class FullscreenWebViewOptionsBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private lateinit var btnMarkSelectedText: ImageButton


    init {
        setupUi()
    }

    private fun setupUi() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.view_fullscreen_options_bar, this)

        btnMarkSelectedText = rootView.btnMarkSelectedText
    }


    /**
     * Hides the button to mark selected text.
     * By default this button is hidden.
     */
    fun hideMarkSelectedTextButton() {
        btnMarkSelectedText.visibility = View.GONE
    }

    /**
     * Shows the button to mark selected text.
     * By default this button is hidden.
     */
    fun showMarkSelectedTextButton(editor: RichTextEditor) {
        setupMarkSelectedTextCommand(editor)

        btnMarkSelectedText.visibility = View.VISIBLE
    }

    private fun setupMarkSelectedTextCommand(editor: RichTextEditor) {
        val command = TextMarkerCommand()
        command.executor = editor.javaScriptExecutor
        command.commandView = AndroidCommandView(btnMarkSelectedText)

        StyleApplier().applyCommandStyle(command.icon, command.style, btnMarkSelectedText)

        btnMarkSelectedText.setOnClickListener { command.commandInvoked() }
    }


    /**
     * This method is simply there so that FullscreenWebView can get rid of focus as otherwise in viewing mode keyboard would show up on each tap.
     */
    fun requestFocusSoIGetRidOfIt() {
        this.optionsBarSearchView.searchField.requestFocus()
    }

}