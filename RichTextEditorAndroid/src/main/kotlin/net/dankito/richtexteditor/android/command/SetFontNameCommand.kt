package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.util.SystemFontsParser


class SetFontNameCommand : SelectValueCommand(Command.FONTNAME, R.drawable.ic_font_download_white_48dp) {


    private val fontInfos = SystemFontsParser().parseSystemFonts()


    override fun initValuesDisplayTexts(): List<CharSequence> {
        val displayTexts = ArrayList<CharSequence>()

        fontInfos.forEach { fontInfo ->
            val fontName = fontInfo.fontName

            var fontDisplayText = fontInfo.fontName
            fontInfo.bestAliasPick?.let { alias ->
                fontDisplayText = "$fontName ($alias)"
            }

            displayTexts.add(getHtmlSpanned("<font face=\"$fontName\">$fontDisplayText</font>"))
        }

        return displayTexts
    }

    override fun valueSelected(editor: RichTextEditor, position: Int) {
        editor.setFontName(fontInfos[position].fontName)
    }

}