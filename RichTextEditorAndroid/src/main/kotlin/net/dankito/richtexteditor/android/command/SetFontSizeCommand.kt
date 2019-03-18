package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.util.FontSizeUtils
import net.dankito.richtexteditor.command.CommandName

class SetFontSizeCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_format_size_white_48dp), protected val utils: FontSizeUtils = FontSizeUtils())
    : SelectValueCommand(CommandName.FONTSIZE, icon) {


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return utils.getValuesDisplayTexts(editor?.context)
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        utils.setFontSize(executor, position)
    }

}