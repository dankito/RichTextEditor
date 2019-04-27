package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.util.TextFormatUtils
import net.dankito.richtexteditor.command.CommandName


open class SetTextFormatCommand(icon: Icon = AndroidIcon(R.drawable.ic_text_format_white_48dp), protected var utils: TextFormatUtils = TextFormatUtils())
    : SelectValueCommand(CommandName.FORMATBLOCK, icon) {


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return utils.getValuesDisplayTexts(editor?.context)
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        utils.setTextFormat(executor, position)
    }

}