package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.RemoveFormatCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class RemoveFormatCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_clear_black_36dp.png")) : RemoveFormatCommandBase(icon)