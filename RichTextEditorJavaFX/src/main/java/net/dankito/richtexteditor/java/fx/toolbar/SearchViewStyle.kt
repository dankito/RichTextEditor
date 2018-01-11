package net.dankito.richtexteditor.java.fx.toolbar

import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.richtexteditor.java.fx.JavaFXIcon


data class SearchViewStyle(
        val toolbarCommandStyle: ToolbarCommandStyle = ToolbarCommandStyle(),
        val searchFieldWidth: Double = 140.0,
        val jumpToPreviousNextResultIconWidth: Double = 18.0,
        val jumpToPreviousResultIcon: JavaFXIcon = JavaFXIcon.fromResourceName("ic_arrow_up_black_36dp.png"),
        val jumpToNextResultIcon: JavaFXIcon = JavaFXIcon.fromResourceName("ic_arrow_down_black_36dp.png")
)