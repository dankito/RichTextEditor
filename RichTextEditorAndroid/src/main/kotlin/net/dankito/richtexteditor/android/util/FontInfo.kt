package net.dankito.richtexteditor.android.util


open class FontInfo(val fontFamily: String) {

    var fontName: String? = null


    override fun toString(): String {
        return "$fontFamily ($fontName)"
    }

}