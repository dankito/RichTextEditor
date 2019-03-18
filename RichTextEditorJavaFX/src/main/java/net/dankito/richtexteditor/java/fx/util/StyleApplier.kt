package net.dankito.richtexteditor.java.fx.util

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.utils.image.ImageReference
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


class StyleApplier {

    fun applyCommandStyle(icon: ImageReference, style: ToolbarCommandStyle, commandView: Region) {
        ((commandView as? Button)?.graphic as? ImageView)?.let { imageView ->
            imageView.isPreserveRatio = true
            imageView.fitHeight = style.heightDp.toDouble()
            imageView.fitWidth = style.widthDp.toDouble()

            (icon as? JavaFXImageReference)?.let { imageView.image = Image(it.imageUrl) }

            commandView.padding = Insets(style.paddingDp.toDouble())

            commandView.prefWidth = style.widthDp.toDouble()
            commandView.minHeight = style.heightDp.toDouble()
            commandView.maxHeight = commandView.minHeight
        }

        HBox.setMargin(commandView, Insets(0.0, style.marginRightDp.toDouble(), 0.0, 0.0))
    }

}