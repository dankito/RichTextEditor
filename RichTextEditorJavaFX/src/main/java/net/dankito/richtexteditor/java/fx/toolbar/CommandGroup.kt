package net.dankito.richtexteditor.java.fx.toolbar

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import tornadofx.*


class CommandGroup : View() {

    override val root = hbox {
        alignment = Pos.CENTER_LEFT
        padding = Insets(2.0, 4.0, 2.0, 4.0)

        background = Background(BackgroundFill(Color.WHITE, CornerRadii(8.0), Insets.EMPTY))
    }

    fun addCommand(commandView: Region) {
        root.add(commandView)
    }

}