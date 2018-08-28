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


    open fun getPreviewTextForCommandValue(commandValue: String): CharSequence {
        var fontFamily = commandValue
        var fontName = findFontNameForFontFamily(fontFamily)

        if(fontFamily.contains(",")) { // sometimes, e. g. when setting text format to 'Heading 1', Editor returns e. g. 'Roboto, sans-serif' as commandValue
            val parts = fontFamily.split(",")
            fontName = parts[0].trim()
            fontFamily = parts[1].trim()
        }

        return getFontPreviewHtml(fontFamily, fontName, false)
    }

    open fun findFontNameForFontFamily(fontFamily: String): String? {
        return fontInfos.firstOrNull { fontFamily == it.fontFamily }?.fontName
    }


    open fun setFontName(executor: JavaScriptExecutorBase, position: Int) {
        executor.setFontName(fontInfos[position].fontFamily)
    }

}