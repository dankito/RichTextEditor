package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.grouped_text_styles_command_view.view.*
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.*
import net.dankito.richtexteditor.command.ToolbarCommand


open class GroupedTextStylesCommandView : GroupedCommandsView {

    constructor(context: Context) : super(context)  { initView() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initView() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView() }


    protected lateinit var textAlignmentToolbar: EditorToolbar

    protected lateinit var basicTextStylesToolbar: EditorToolbar


    protected open fun initView() {
        val contentView = View.inflate(context, R.layout.grouped_text_styles_command_view, null)
        addContentView(contentView)

        textAlignmentToolbar = contentView.textAlignmentToolbar
        basicTextStylesToolbar = contentView.basicTextStylesToolbar

        setupTextAlignmentToolbar()
        setupBasicTextStylesToolbar()
    }

    protected open fun setupTextAlignmentToolbar() {
        textAlignmentToolbar.addCommand(AlignLeftCommand())
        textAlignmentToolbar.addCommand(AlignCenterCommand())
        textAlignmentToolbar.addCommand(AlignRightCommand())
        textAlignmentToolbar.addCommand(AlignJustifyCommand())

        textAlignmentToolbar.addSpace()
        textAlignmentToolbar.addCommand(DecreaseIndentCommand())
        textAlignmentToolbar.addCommand(IncreaseIndentCommand())
    }

    protected open fun setupBasicTextStylesToolbar() {
        basicTextStylesToolbar.addCommand(BoldCommand())
        basicTextStylesToolbar.addCommand(ItalicCommand())
        basicTextStylesToolbar.addCommand(UnderlineCommand())
        basicTextStylesToolbar.addCommand(StrikeThroughCommand())
        basicTextStylesToolbar.addCommand(SubscriptCommand())
        basicTextStylesToolbar.addCommand(SuperscriptCommand())
        basicTextStylesToolbar.addCommand(RemoveFormatCommand())

        basicTextStylesToolbar.addSpace()
        basicTextStylesToolbar.addCommand(RedoCommand())
    }


    override fun initialize(editor: RichTextEditor, command: ToolbarCommand) {
        textAlignmentToolbar.editor = editor
        basicTextStylesToolbar.editor = editor

        super.initialize(editor, command)
    }

}