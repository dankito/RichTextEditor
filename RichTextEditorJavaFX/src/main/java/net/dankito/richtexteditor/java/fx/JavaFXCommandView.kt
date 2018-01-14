package net.dankito.richtexteditor.java.fx

import com.sun.javafx.scene.control.skin.ColorPickerSkin
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Region
import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.CommandView
import net.dankito.richtexteditor.java.fx.extensions.setImageTintColor
import net.dankito.richtexteditor.java.fx.extensions.toJavaFXColor


class JavaFXCommandView(private val node: Region) : CommandView() {

    override fun setIsEnabled(isEnabled: Boolean) {
        node.isDisable = !isEnabled
    }

    override fun setBackgroundColor(color: Color) {
        node.background = Background(BackgroundFill(color.toJavaFXColor(), CornerRadii(4.0), Insets.EMPTY))
    }

    override fun setTintColor(color: Color) {
        ((node as? Button)?.graphic as? ImageView)?.let { imageView ->
            imageView.setImageTintColor(color)
        }

        (node as? ColorPicker)?.let { colorPicker ->
            (colorPicker.skin as? ColorPickerSkin)?.displayNode?.style = "-fx-text-fill: ${color.toHexColorString()} ;"
        }
    }

}