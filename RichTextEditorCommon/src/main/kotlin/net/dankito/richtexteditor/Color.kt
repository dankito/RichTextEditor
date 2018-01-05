package net.dankito.richtexteditor


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
    }


    fun toInt(): Int {
//        (colorByteArr[0] << 24) + ((colorByteArr[1] & 0xFF) << 16) + ((colorByteArr[2] & 0xFF) << 8) + (colorByteArr[3] & 0xFF)

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