package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.RedoCommandBase


open class RedoCommand(icon: Icon = AndroidIcon(R.drawable.ic_redo_white_48dp)) : RedoCommandBase(icon)