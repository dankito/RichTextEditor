package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.DecreaseIndentCommandBase


open class DecreaseIndentCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_indent_decrease_white_48dp)) : DecreaseIndentCommandBase(icon)