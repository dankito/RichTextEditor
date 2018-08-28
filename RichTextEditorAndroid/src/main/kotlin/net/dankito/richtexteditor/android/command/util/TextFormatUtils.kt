package net.dankito.richtexteditor.android.command.util

import android.content.Context
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.utils.extensions.HtmlExtensions


open class TextFormatUtils {

    open fun getValuesDisplayTexts(context: Context?): List<CharSequence> {
        context?.let {
            return listOf(
                    getSpannedFromHtml(context, R.string.text_format_header_1),
                    getSpannedFromHtml(context, R.string.text_format_header_2),
                    getSpannedFromHtml(context, R.string.text_format_header_3),
                    getSpannedFromHtml(context, R.string.text_format_header_4),
                    getSpannedFromHtml(context, R.string.text_format_header_5),
                    getSpannedFromHtml(context, R.string.text_format_header_6),
                    getSpannedFromHtml(context, R.string.text_format_paragraph),
                    getSpannedFromHtml(context, R.string.text_format_preformat),
                    getSpannedFromHtml(context, R.string.text_format_block_quote)
            )
        }

        return emptyList()
    }

    protected open fun getSpannedFromHtml(context: Context, stringResourceId: Int): CharSequence {
        return HtmlExtensions.getSpannedFromHtml(context, stringResourceId)
    }


    open fun setTextFormat(executor: JavaScriptExecutorBase, position: Int) {
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