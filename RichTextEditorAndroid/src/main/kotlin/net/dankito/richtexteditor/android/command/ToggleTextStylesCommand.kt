package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.CommandName


class ToggleTextStylesCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_bold_white_48dp))
    : ToggleGroupedCommandsViewCommand(CommandName.TOOGLETEXTSTYLESGROUPEDCOMMANDSVIEW, icon)