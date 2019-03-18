package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.DecreaseIndentCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


class DecreaseIndentCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_indent_decrease_black_36dp.png")) : DecreaseIndentCommandBase(icon)