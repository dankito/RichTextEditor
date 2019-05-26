package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.IncreaseIndentCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class IncreaseIndentCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_indent_increase_black_36dp.png")) : IncreaseIndentCommandBase(icon)