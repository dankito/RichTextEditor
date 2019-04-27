package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.StrikeThroughCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


open class StrikeThroughCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_format_strikethrough_black_36dp.png")) : StrikeThroughCommandBase(icon)