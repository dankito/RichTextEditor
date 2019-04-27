package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.RedoCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


open class RedoCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_redo_black_36dp.png")) : RedoCommandBase(icon)