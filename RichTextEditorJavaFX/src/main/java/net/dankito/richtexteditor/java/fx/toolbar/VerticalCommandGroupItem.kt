package net.dankito.richtexteditor.java.fx.toolbar

import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.java.fx.JavaFXIcon


data class VerticalCommandGroupItem(val command: ToolbarCommand, val title: String, val icon: JavaFXIcon)