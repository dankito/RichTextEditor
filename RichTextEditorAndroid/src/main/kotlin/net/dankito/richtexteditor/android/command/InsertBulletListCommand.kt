package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.InsertBulletListCommandBase


open class InsertBulletListCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_list_bulleted_white_48dp)) : InsertBulletListCommandBase(icon)