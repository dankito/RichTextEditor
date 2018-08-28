package net.dankito.richtexteditor.android.command.util

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.util.SystemFontsParser
import net.dankito.utils.extensions.getSpannedFromHtml


open class FontNameUtils {


    private val fontInfos = SystemFontsParser().parseSystemFonts()


    open fun getAvailableFontsPreviews(): List<CharSequence> {
        return fontInfos.map { getFontPreviewHtml(it.fontFamily, it.fontName, true) }
    }

    open fun getFontPreviewHtml(fontFamily: String, fontName: String?, showFamilyAndFontName: Boolean): CharSequence {
        var fontDisplayText = fontFamily
        fontName?.let {
            if(showFamilyAndFontName) {
                fontDisplayText = "$fontFamily ($fontName)"
            }
            else {
                fontDisplayText = fontName
            }
        }

        return "<font face=\"$fontFamily\">$fontDisplayText</font>".getSpannedFromHtml()
    }


    open fun findFontNameForFontFamily(fontFamily: String): String? {
        fontInfos.forEach { fontInfo ->
            if(fontFamily == fontInfo.fontFamily) {
                return fontInfo.fontName
            }
        }

        return null
    }


    open fun setFontName(executor: JavaScriptExecutorBase, position: Int) {
        executor.setFontName(fontInfos[position].fontFamily)
    }

}