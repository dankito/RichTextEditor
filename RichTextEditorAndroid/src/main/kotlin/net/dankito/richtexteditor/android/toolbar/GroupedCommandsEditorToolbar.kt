package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import net.dankito.richtexteditor.android.command.*


class GroupedCommandsEditorToolbar : EditorToolbar {

    constructor(context: Context) : super(context) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initToolbar() }


    protected open fun initToolbar() {
        addCommand(ToggleGroupedTextStylesCommand())

        addCommand(TextMarkerCommand())

        addCommand(UndoCommand())

        addCommand(ToggleGroupedInsertCommandsCommand())

        addCommand(EnterViewingModeCommand())

        addSearchView()
    }

}