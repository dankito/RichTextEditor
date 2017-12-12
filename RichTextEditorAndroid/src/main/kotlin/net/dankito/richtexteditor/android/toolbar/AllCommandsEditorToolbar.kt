package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import net.dankito.richtexteditor.android.command.*


class AllCommandsEditorToolbar : EditorToolbar {

    constructor(context: Context) : super(context) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initToolbar() }



    private fun initToolbar() {
        addCommand(BoldCommand())
        addCommand(ItalicCommand())
        addCommand(UnderlineCommand())
        addCommand(StrikeThroughCommand())
        addCommand(SuperscriptCommand())
        addCommand(SubscriptCommand())
        addCommand(RemoveFormatCommand())

        addCommand(UndoCommand())
        addCommand(RedoCommand())

        addCommand(BlockQuoteCommand())
        addCommand(SetTextFormatCommand())
        addCommand(SetFontNameCommand())
        addCommand(SetFontSizeCommand())
        addCommand(SwitchTextColorOnOffCommand())
        addCommand(SetTextColorCommand())
        addCommand(SwitchTextBackgroundColorOnOffCommand())
        addCommand(SetTextBackgroundColorCommand())

        addCommand(DecreaseIndentCommand())
        addCommand(IncreaseIndentCommand())

        addCommand(AlignLeftCommand())
        addCommand(AlignCenterCommand())
        addCommand(AlignRightCommand())
        addCommand(AlignJustifyCommand())

        addCommand(InsertBulletListCommand())
        addCommand(InsertNumberedListCommand())

        addCommand(InsertLinkCommand())
        addCommand(InsertImageCommand())
        addCommand(InsertCheckboxCommand())
    }

}