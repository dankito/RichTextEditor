package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.util.TextFormatUtils
import net.dankito.richtexteditor.command.CommandName


class SetTextFormatCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_text_format_white_48dp), protected var utils: TextFormatUtils = TextFormatUtils())
    : SelectValueCommand(CommandName.FORMATBLOCK, icon) {


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return utils.getValuesDisplayTexts(editor?.context)
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        utils.setTextFormat(executor, position)
    }

}