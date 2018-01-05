package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.UndoCommandBase


class UndoCommand(icon: Icon = AndroidIcon(R.drawable.ic_undo_white_48dp)) : UndoCommandBase(icon)