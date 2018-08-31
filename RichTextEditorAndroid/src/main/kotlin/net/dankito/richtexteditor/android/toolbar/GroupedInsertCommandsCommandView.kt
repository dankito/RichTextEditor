package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.grouped_insert_commands_command_view.view.*
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.*
import net.dankito.richtexteditor.android.extensions.hideView
import net.dankito.richtexteditor.command.ToolbarCommand


open class GroupedInsertCommandsCommandView : GroupedCommandsView {

    constructor(context: Context) : super(context)  { initView() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initView() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView() }


    protected lateinit var insertCommandsToolbar: EditorToolbar


    protected open fun initView() {
        val contentView = View.inflate(context, R.layout.grouped_insert_commands_command_view, null)
        addContentViewAndSetBackgroundToPrimaryColor(contentView, true)

        insertCommandsToolbar = contentView.insertCommandsToolbar

        setupInsertToolbar()
    }

    protected open fun setupInsertToolbar() {
        insertCommandsToolbar.addCommand(InsertBulletListCommand())
        insertCommandsToolbar.addCommand(InsertNumberedListCommand())

        insertCommandsToolbar.addCommand(InsertLinkCommand())
        insertCommandsToolbar.addCommand(InsertImageCommand())
        insertCommandsToolbar.addCommand(InsertCheckboxCommand())


        insertCommandsToolbar.addCommandInvokedListener {
            hideView()
        }
    }


    override fun initialize(editor: RichTextEditor, command: ToolbarCommand) {
        insertCommandsToolbar.editor = editor

        super.initialize(editor, command)
    }

}