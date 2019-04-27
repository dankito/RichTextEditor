package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.InsertNumberedListCommandBase


open class InsertNumberedListCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_list_numbered_white_48dp)) : InsertNumberedListCommandBase(icon)