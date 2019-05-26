package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.UndoCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class UndoCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_undo_black_36dp.png")) : UndoCommandBase(icon)