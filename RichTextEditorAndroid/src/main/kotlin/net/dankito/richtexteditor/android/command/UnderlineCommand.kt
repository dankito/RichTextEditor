package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.UnderlineCommandBase


open class UnderlineCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_underlined_white_48dp)) : UnderlineCommandBase(icon)