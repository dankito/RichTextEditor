package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.IncreaseIndentCommandBase


open class IncreaseIndentCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_indent_increase_white_48dp)) : IncreaseIndentCommandBase(icon)