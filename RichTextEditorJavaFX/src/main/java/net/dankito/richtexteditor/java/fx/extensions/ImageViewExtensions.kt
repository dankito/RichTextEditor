package net.dankito.richtexteditor.java.fx.extensions

import javafx.scene.effect.ColorAdjust
import javafx.scene.image.ImageView
import net.dankito.utils.Color


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

    colorAdjust.hue = toColorAdjustValue(hsv[0]) // hsv values range from 0.0 to 1.0, ColorAdjust values range from -1.0 to 1.0
    colorAdjust.saturation = toColorAdjustValue(hsv[1])
    colorAdjust.brightness = toColorAdjustValue(hsv[2])

    this.effect = colorAdjust
}

private fun toColorAdjustValue(hsvValue: Double): Double {
    return (hsvValue * 2) - 1.0
}
