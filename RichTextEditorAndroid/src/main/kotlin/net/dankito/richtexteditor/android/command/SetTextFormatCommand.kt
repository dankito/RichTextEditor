package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import java.util.*


class SetTextFormatCommand : SelectValueCommand(Commands.FORMATBLOCK, R.drawable.ic_text_format_white_48dp) {


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return Arrays.asList(
                getHtmlSpanned(R.string.text_format_header_1),
                getHtmlSpanned(R.string.text_format_header_2),
                getHtmlSpanned(R.string.text_format_header_3),
                getHtmlSpanned(R.string.text_format_header_4),
                getHtmlSpanned(R.string.text_format_header_5),
                getHtmlSpanned(R.string.text_format_header_6),
                getHtmlSpanned(R.string.text_format_paragraph),
                getHtmlSpanned(R.string.text_format_preformat),
                getHtmlSpanned(R.string.text_format_block_quote)
        )
    }

    override fun valueSelected(editor: RichTextEditor, position: Int) {
        when(position) {
            0 -> editor.setHeading(1)
            1 -> editor.setHeading(2)
            2 -> editor.setHeading(3)
            3 -> editor.setHeading(4)
            4 -> editor.setHeading(5)
            5 -> editor.setHeading(6)
            6 -> editor.setFormattingToParagraph()
            7 -> editor.setPreformat()
            8 -> editor.setBlockQuote()
        }
    }

}