package net.dankito.richtexteditor.java.fx.extensions

import net.dankito.richtexteditor.Color


fun Color.toJavaFXColor(): javafx.scene.paint.Color {
    return javafx.scene.paint.Color(convertColorValue(red), convertColorValue(green),
            convertColorValue(blue), convertColorValue(alpha))
}

private fun convertColorValue(intFrom0To255: Int): Double {
    return intFrom0To255 / 255.0
}