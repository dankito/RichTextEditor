package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.GroupedCommandsView
import net.dankito.richtexteditor.android.toolbar.GroupedInsertCommandsCommandView
import net.dankito.richtexteditor.command.CommandName


open class ToggleGroupedInsertCommandsCommand(icon: Icon = AndroidIcon(R.drawable.ic_insert_photo_white_48dp))
    : ToggleGroupedCommandsViewCommand(CommandName.TOGGLE_GROUPED_INSERT_COMMANDS_COMMANDS_VIEW, icon) {


    override fun createGroupedCommandsView(editor: RichTextEditor): GroupedCommandsView {
        return GroupedInsertCommandsCommandView(editor.context)
    }

}