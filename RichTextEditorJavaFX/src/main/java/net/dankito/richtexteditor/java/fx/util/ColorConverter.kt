package net.dankito.richtexteditor.java.fx.util

import net.dankito.richtexteditor.Color


class ColorConverter {

    fun convertJavaFXColor(javaFXColor: javafx.scene.paint.Color): Color {
        return Color(convertColorComponent(javaFXColor.red), convertColorComponent(javaFXColor.green),
                convertColorComponent(javaFXColor.blue), convertColorComponent(javaFXColor.opacity))
    }

    private fun convertColorComponent(component: Double): Int {
        return (component * 255).toInt()
    }

}