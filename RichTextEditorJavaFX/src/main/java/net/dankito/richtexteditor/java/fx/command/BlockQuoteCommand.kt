package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.BlockQuoteCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


open class BlockQuoteCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_quote_black_36dp.png")) : BlockQuoteCommandBase(icon)