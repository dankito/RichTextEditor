package net.dankito.richtexteditor.java.fx.extensions

import javafx.scene.effect.ColorAdjust
import javafx.scene.image.ImageView
import net.dankito.richtexteditor.Color


fun ImageView.setImageTintColor(color: Color) {
    val hsv = color.toHSV()

    setImageTintColor(hsv)
}

fun ImageView.setImageTintColor(color: javafx.scene.paint.Color) {
    val hsv = doubleArrayOf(color.hue, color.saturation, color.brightness)

    setImageTintColor(hsv)
}

private fun ImageView.setImageTintColor(hsv: DoubleArray) {
    val colorAdjust = ColorAdjust()
    colorAdjust.hue = hsv[0]
    colorAdjust.saturation = hsv[1]
    colorAdjust.brightness = hsv[2]

    this.effect = colorAdjust
}