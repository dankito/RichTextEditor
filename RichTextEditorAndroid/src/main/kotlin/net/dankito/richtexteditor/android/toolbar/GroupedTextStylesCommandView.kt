package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.grouped_text_styles_command_view.view.*
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.*
import net.dankito.richtexteditor.command.ToolbarCommand


open class GroupedTextStylesCommandView : GroupedCommandsView {

    constructor(context: Context) : super(context)  { init() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }


    private lateinit var view: View

    private lateinit var textAlignmentToolbar: EditorToolbar

    private lateinit var basicTextStylesToolbar: EditorToolbar


    private fun init() {
        this.view = View.inflate(context, R.layout.grouped_text_styles_command_view, null)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.addRule(RelativeLayout.ALIGN_PARENT_START)
            params.addRule(RelativeLayout.ALIGN_PARENT_END)
        }

        addView(view, params)

        textAlignmentToolbar = view.textAlignmentToolbar

        textAlignmentToolbar.addCommand(DecreaseIndentCommand())
        textAlignmentToolbar.addCommand(IncreaseIndentCommand())

        textAlignmentToolbar.addCommand(AlignLeftCommand())
        textAlignmentToolbar.addCommand(AlignCenterCommand())
        textAlignmentToolbar.addCommand(AlignRightCommand())
        textAlignmentToolbar.addCommand(AlignJustifyCommand())

        basicTextStylesToolbar = view.basicTextStylesToolbar
        basicTextStylesToolbar.addCommand(BoldCommand())
        basicTextStylesToolbar.addCommand(ItalicCommand())
        basicTextStylesToolbar.addCommand(UnderlineCommand())
        basicTextStylesToolbar.addCommand(StrikeThroughCommand())
        basicTextStylesToolbar.addCommand(SubscriptCommand())
        basicTextStylesToolbar.addCommand(SuperscriptCommand())
        basicTextStylesToolbar.addCommand(RemoveFormatCommand())
    }

    override fun initialize(editor: RichTextEditor, command: ToolbarCommand) {
        textAlignmentToolbar.editor = editor
        basicTextStylesToolbar.editor = editor

        super.initialize(editor, command)
    }

}