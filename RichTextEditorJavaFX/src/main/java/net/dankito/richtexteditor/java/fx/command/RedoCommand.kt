package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.RedoCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class RedoCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_redo_black_36dp.png")) : RedoCommandBase(icon)