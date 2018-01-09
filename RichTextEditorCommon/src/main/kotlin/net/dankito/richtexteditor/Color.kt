package net.dankito.richtexteditor

import org.slf4j.LoggerFactory


class Color(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255) {

    companion object {
        val White = Color(255, 255, 255)
        val Black = Color(0, 0, 0)

        val LightGray = Color(204, 204, 204)
        val DarkGray = Color(68, 68, 68)

        val Red = Color(255, 0, 0)

        val Yellow = Color(255, 255, 0)

        val Transparent = Color(0, 0, 0, 0)


        fun fromInt(rgb: Int): Color {
            val red = rgb shr 16 and 0xFF
            val green = rgb shr 8 and 0xFF
            val blue = rgb and 0xFF

            return Color(red, green, blue)
        }

        private val log = LoggerFactory.getLogger(Color::class.java)
    }


    fun toInt(): Int {
        var argb = alpha
        argb = (argb shl 8) + red
        argb = (argb shl 8) + green
        argb = (argb shl 8) + blue

        return argb
    }

    fun toHexColorString(): String {
        if(alpha == 255) { // without alpha
            return String.format("#%06X", 0xFFFFFF and toInt())
        }
        else {
            return "rgba($red, $green, $blue, $alpha)"
        }
    }

    // copied from https://stackoverflow.com/questions/2399150/convert-rgb-value-to-hsv
    fun toHSV(): DoubleArray {
        val red = this.red / 255.0
        val green = this.green / 255.0
        val blue = this.blue / 255.0

        var h: Double
        val s: Double
        val v: Double

        val min: Double = Math.min(Math.min(red, green), blue)
        val max: Double = Math.max(Math.max(red, green), blue)
        val delta: Double = max - min

        // V
        v = max

        // S
        if(max != 0.0) {
            s = delta / max
        }
        else {
            s = 0.0
            h = 0.0
            return doubleArrayOf(h, s, v)
        }

        // H
        if(red == max) {
            h = (green - blue) / delta // between yellow & magenta
        }
        else if(green == max) {
            h = 2 + (blue - red) / delta // between cyan & yellow
        }
        else {
            h = 4 + (red - green) / delta // between magenta & cyan
        }

        if(Double.NaN.equals(h)) {
            h = 0.0
        }

        h *= 60.0    // degrees

        if(h < 0) {
            h += 360.0
        }

        return doubleArrayOf(h, s, v)
    }


    override fun equals(other: Any?): Boolean {
        if(other is Color) {
            return red == other.red && green == other.green &&
                    blue == other.blue && alpha == other.alpha
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = red
        result = 31 * result + green
        result = 31 * result + blue
        result = 31 * result + alpha
        return result
    }


    override fun toString(): String {
        return "red: $red, green: $green, blue: $blue, alpha: $alpha"
    }

}