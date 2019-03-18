package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.UndoCommandBase


class UndoCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_undo_white_48dp)) : UndoCommandBase(icon)