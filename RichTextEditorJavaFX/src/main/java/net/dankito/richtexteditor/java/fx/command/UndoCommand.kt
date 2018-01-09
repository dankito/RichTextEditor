package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.UndoCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


class UndoCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_undo_black_36dp.png")) : UndoCommandBase(icon)