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


    protected lateinit var basicTextStylesToolbar: EditorToolbar

    protected lateinit var textFormatToolbar: EditorToolbar

    protected lateinit var textColorAndAlignmentToolbar: EditorToolbar


    protected open fun initView() {
        val contentView = View.inflate(context, R.layout.grouped_text_styles_command_view, null)
        addContentViewAndSetBackgroundToPrimaryColor(contentView, true)

        basicTextStylesToolbar = contentView.basicTextStylesToolbar
        textFormatToolbar = contentView.textFormatToolbar
        textColorAndAlignmentToolbar = contentView.textColorAndAlignmentToolbar

        setupBasicTextStylesToolbar()
        setupTextFormatToolbar()
        setupTextAlignmentToolbar()
    }

    protected open fun setupBasicTextStylesToolbar() {
        basicTextStylesToolbar.addCommand(BoldCommand())
        basicTextStylesToolbar.addCommand(ItalicCommand())
        basicTextStylesToolbar.addCommand(UnderlineCommand())
        basicTextStylesToolbar.addCommand(StrikeThroughCommand())

        basicTextStylesToolbar.addSpace()
        basicTextStylesToolbar.addCommand(SubscriptCommand())
        basicTextStylesToolbar.addCommand(SuperscriptCommand())
        basicTextStylesToolbar.addCommand(RemoveFormatCommand())

        basicTextStylesToolbar.addSpace()
        basicTextStylesToolbar.addCommand(RedoCommand())
    }

    protected open fun setupTextFormatToolbar() {
        textFormatToolbar.addCommand(SetTextFormatWithPreviewCommand())

        textFormatToolbar.addCommand(SetFontNameWithPreviewCommand())

        textFormatToolbar.addCommand(SetFontSizeWithPreviewCommand())
    }

    protected open fun setupTextAlignmentToolbar() {
        textColorAndAlignmentToolbar.addCommand(SetTextColorCommand())
        textColorAndAlignmentToolbar.addCommand(SetTextBackgroundColorCommand())
        textColorAndAlignmentToolbar.addSpace()

        textColorAndAlignmentToolbar.addCommand(AlignLeftCommand())
        textColorAndAlignmentToolbar.addCommand(AlignCenterCommand())
        textColorAndAlignmentToolbar.addCommand(AlignRightCommand())
        textColorAndAlignmentToolbar.addCommand(AlignJustifyCommand())

        textColorAndAlignmentToolbar.addSpace()
        textColorAndAlignmentToolbar.addCommand(DecreaseIndentCommand())
        textColorAndAlignmentToolbar.addCommand(IncreaseIndentCommand())
    }


    override fun initialize(editor: RichTextEditor, command: ToolbarCommand) {
        basicTextStylesToolbar.editor = editor
        textFormatToolbar.editor = editor
        textColorAndAlignmentToolbar.editor = editor

        super.initialize(editor, command)
    }

}