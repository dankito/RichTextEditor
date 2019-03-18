package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.util.FontSizeUtils
import net.dankito.richtexteditor.command.CommandName
import net.dankito.utils.android.extensions.getPlainTextFromHtml

open class SetFontSizeWithPreviewCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_format_size_white_48dp), protected var utils: FontSizeUtils = FontSizeUtils())
    : SelectValueWithPreviewCommand(CommandName.FONTSIZE, icon) {


    override fun getDefaultPreview(): CharSequence {
        return getPreviewTextForCommandValue("4")
    }

    override fun getPreviewTextForCommandValue(commandValue: String): CharSequence {
        var fontSizeHtml = getValuesDisplayTexts().get(3)
        try {
            val position = commandValue.toInt() - 1 // font sizes start at 1, position at 0

            fontSizeHtml = getValuesDisplayTexts().get(position)
        } catch (e: Exception) { }

        return fontSizeHtml.toString().getPlainTextFromHtml()
    }


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return utils.getValuesDisplayTexts(editor?.context)
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        utils.setFontSize(executor, position)
    }

}