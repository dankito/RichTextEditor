package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.ItalicCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class ItalicCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_italic_black_36dp.png")) : ItalicCommandBase(icon)