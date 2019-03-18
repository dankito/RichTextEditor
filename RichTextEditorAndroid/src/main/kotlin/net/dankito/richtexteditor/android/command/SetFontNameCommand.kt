package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.util.FontNameUtils
import net.dankito.richtexteditor.command.CommandName


class SetFontNameCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_font_download_white_48dp), protected var utils: FontNameUtils = FontNameUtils())
    : SelectValueCommand(CommandName.FONTNAME, icon) {


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return utils.getAvailableFontsPreviews()
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        utils.setFontName(executor, position)
    }

}