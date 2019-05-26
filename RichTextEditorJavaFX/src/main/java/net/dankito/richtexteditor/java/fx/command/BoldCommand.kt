package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.BoldCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class BoldCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_bold_black_36dp.png")) : BoldCommandBase(icon)