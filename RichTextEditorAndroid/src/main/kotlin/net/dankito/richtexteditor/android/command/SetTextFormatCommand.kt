package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.CommandName
import java.util.*


class SetTextFormatCommand(icon: Icon = AndroidIcon(R.drawable.ic_text_format_white_48dp)) : SelectValueCommand(CommandName.FORMATBLOCK, icon) {


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

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        when(position) {
            0 -> executor.setHeading(1)
            1 -> executor.setHeading(2)
            2 -> executor.setHeading(3)
            3 -> executor.setHeading(4)
            4 -> executor.setHeading(5)
            5 -> executor.setHeading(6)
            6 -> executor.setFormattingToParagraph()
            7 -> executor.setPreformat()
            8 -> executor.setBlockQuote()
        }
    }

}