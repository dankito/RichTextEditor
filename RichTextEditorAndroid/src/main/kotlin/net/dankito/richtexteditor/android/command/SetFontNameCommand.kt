package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.util.SystemFontsParser


class SetFontNameCommand : SelectValueCommand(Commands.FONTNAME, R.drawable.ic_font_download_white_48dp) {


    private val fontNames = SystemFontsParser().parseSystemFonts()


    override fun initValuesDisplayTexts(): List<CharSequence> {
        val displayTexts = ArrayList<CharSequence>()

        fontNames.forEach { fontName ->
            displayTexts.add(getHtmlSpanned("<font face=\"$fontName\">$fontName</font>"))
        }

        return displayTexts
    }

    override fun valueSelected(editor: RichTextEditor, position: Int) {
        editor.setFontName(fontNames[position])
    }

}