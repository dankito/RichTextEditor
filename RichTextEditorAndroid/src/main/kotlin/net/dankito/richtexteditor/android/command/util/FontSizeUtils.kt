package net.dankito.richtexteditor.android.command.util

import android.content.Context
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.R
import net.dankito.utils.android.extensions.HtmlExtensions


open class FontSizeUtils {

    open fun getValuesDisplayTexts(context: Context?): List<CharSequence> {
        context?.let {
            return listOf(
                    getSpannedFromHtml(context, R.string.font_size_very_very_small),
                    getSpannedFromHtml(context, R.string.font_size_very_small),
                    getSpannedFromHtml(context, R.string.font_size_small),
                    getSpannedFromHtml(context, R.string.font_size_medium),
                    getSpannedFromHtml(context, R.string.font_size_large),
                    getSpannedFromHtml(context, R.string.font_size_very_large),
                    getSpannedFromHtml(context, R.string.font_size_very_very_large)
            )
        }

        return emptyList()
    }

    protected open fun getSpannedFromHtml(context: Context, stringResourceId: Int): CharSequence {
        return HtmlExtensions.getSpannedFromHtml(context, stringResourceId)
    }


    open fun setFontSize(executor: JavaScriptExecutorBase, position: Int) {
        val fontSize = position + 1 // position starts at 0, font sizes at 1

        executor.setFontSize(fontSize)
    }

}