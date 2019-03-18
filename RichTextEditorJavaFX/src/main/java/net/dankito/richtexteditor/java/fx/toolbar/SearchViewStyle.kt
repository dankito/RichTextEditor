package net.dankito.richtexteditor.java.fx.toolbar

import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


data class SearchViewStyle(
        val toolbarCommandStyle: ToolbarCommandStyle = ToolbarCommandStyle(),
        val searchFieldWidth: Double = 140.0,
        val searchFieldFontSize: Double = 13.0,
        val jumpToPreviousNextResultIconWidth: Double = 18.0,
        val jumpToPreviousResultIcon: JavaFXImageReference = JavaFXImageReference.fromIconsResourceName("ic_arrow_up_black_36dp.png"),
        val jumpToNextResultIcon: JavaFXImageReference = JavaFXImageReference.fromIconsResourceName("ic_arrow_down_black_36dp.png")
)