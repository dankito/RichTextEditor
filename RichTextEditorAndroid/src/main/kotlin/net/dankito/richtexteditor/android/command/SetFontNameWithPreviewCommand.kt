package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.util.FontNameUtils
import net.dankito.richtexteditor.command.CommandName


open class SetFontNameWithPreviewCommand(icon: Icon = AndroidIcon(R.drawable.ic_font_download_white_48dp), protected var utils: FontNameUtils = FontNameUtils())
    : SelectValueWithPreviewCommand(CommandName.FONTNAME, icon) {


    override fun getDefaultPreview(): CharSequence {
        return getPreviewTextForCommandValue("sans-serif")
    }

    override fun getPreviewTextForCommandValue(commandValue: String): CharSequence {
        return utils.getPreviewTextForCommandValue(commandValue)
    }

    override fun initValuesDisplayTexts(): List<CharSequence> {
        return utils.getAvailableFontsPreviews()
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        utils.setFontName(executor, position)
    }

}