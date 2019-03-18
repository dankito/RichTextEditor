package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.UnderlineCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


class UnderlineCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_underlined_black_36dp.png")) : UnderlineCommandBase(icon)