package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import java.util.*

class SetFontSizeCommand : SelectValueCommand(Command.FONTSIZE, R.drawable.ic_format_size_white_48dp) {


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return Arrays.asList(
                getHtmlSpanned(R.string.font_size_very_very_small),
                getHtmlSpanned(R.string.font_size_very_small),
                getHtmlSpanned(R.string.font_size_small),
                getHtmlSpanned(R.string.font_size_medium),
                getHtmlSpanned(R.string.font_size_large),
                getHtmlSpanned(R.string.font_size_very_large),
                getHtmlSpanned(R.string.font_size_very_very_large)
        )
    }

    override fun valueSelected(editor: RichTextEditor, position: Int) {
        val fontSize = position + 1 // position starts at 0, font sizes at 1

        editor.setFontSize(fontSize)
    }

}