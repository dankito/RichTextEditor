package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.GroupedCommandsView
import net.dankito.richtexteditor.android.toolbar.GroupedTextStylesCommandView
import net.dankito.richtexteditor.command.CommandName


open class ToggleGroupedTextStylesCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_bold_white_48dp))
    : ToggleGroupedCommandsViewCommand(CommandName.TOGGLE_GROUPED_TEXT_STYLES_COMMANDS_VIEW, icon) {


    override fun createGroupedCommandsView(editor: RichTextEditor): GroupedCommandsView {
        return GroupedTextStylesCommandView(editor.context)
    }

}