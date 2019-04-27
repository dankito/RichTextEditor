package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.IncreaseIndentCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


open class IncreaseIndentCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_indent_increase_black_36dp.png")) : IncreaseIndentCommandBase(icon)