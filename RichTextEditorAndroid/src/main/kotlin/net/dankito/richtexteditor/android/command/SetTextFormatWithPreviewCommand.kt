package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.util.TextFormatUtils
import net.dankito.richtexteditor.command.CommandName


open class SetTextFormatWithPreviewCommand(icon: Icon = AndroidIcon(R.drawable.ic_text_format_white_48dp), protected var utils: TextFormatUtils = TextFormatUtils())
    : SelectValueWithPreviewCommand(CommandName.FORMATBLOCK, icon) {

    override fun getDefaultPreview(): CharSequence {
        return getPreviewTextForCommandValue("p")
    }

    override fun getPreviewTextForCommandValue(commandValue: String): CharSequence {
        return utils.getPreviewTextForCommandValue(editor?.context, commandValue)
    }

    override fun initValuesDisplayTexts(): List<CharSequence> {
        return utils.getValuesDisplayTexts(editor?.context)
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        utils.setTextFormat(executor, position)
    }

}