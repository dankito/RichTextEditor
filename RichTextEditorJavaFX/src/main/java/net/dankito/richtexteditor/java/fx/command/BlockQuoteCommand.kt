package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.BlockQuoteCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class BlockQuoteCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_quote_black_36dp.png")) : BlockQuoteCommandBase(icon)