package net.dankito.richtexteditor.java.fx.extensions

import net.dankito.utils.Color


fun Color.toJavaFXColor(): javafx.scene.paint.Color {
    return javafx.scene.paint.Color(convertColorValue(red), convertColorValue(green),
            convertColorValue(blue), convertColorValue(alpha))
}

private fun convertColorValue(intFrom0To255: Int): Double {
    return intFrom0To255 / 255.0
}


fun javafx.scene.paint.Color.fromJavaFXColor(): Color {
    return Color(convertFromJavaFXColorValue(red), convertFromJavaFXColorValue(green),
            convertFromJavaFXColorValue(blue), convertFromJavaFXColorValue(opacity))
}

private fun convertFromJavaFXColorValue(doubleFrom0To1: Double): Int {
    return (doubleFrom0To1 * 255.0).toInt()
}