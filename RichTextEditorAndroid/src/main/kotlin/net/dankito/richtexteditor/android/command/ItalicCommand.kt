package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.ItalicCommandBase


open class ItalicCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_italic_white_48dp)) : ItalicCommandBase(icon)