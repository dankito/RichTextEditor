package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.util.SystemFontsParser
import net.dankito.richtexteditor.command.Command


class SetFontNameCommand(iconResourceId: Int = R.drawable.ic_font_download_white_48dp) : SelectValueCommand(Command.FONTNAME, iconResourceId) {


    private val fontInfos = SystemFontsParser().parseSystemFonts()


    override fun initValuesDisplayTexts(): List<CharSequence> {
        val displayTexts = ArrayList<CharSequence>()

        fontInfos.forEach { fontInfo ->
            val fontFamily = fontInfo.fontFamily

            var fontDisplayText = fontInfo.fontFamily
            fontInfo.fontName?.let { fontName ->
                fontDisplayText = "$fontFamily ($fontName)"
            }

            displayTexts.add(getHtmlSpanned("<font face=\"$fontFamily\">$fontDisplayText</font>"))
        }

        return displayTexts
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        executor.setFontName(fontInfos[position].fontFamily)
    }

}