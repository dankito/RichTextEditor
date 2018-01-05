package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command
import java.util.*

class SetFontSizeCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_size_white_48dp)) : SelectValueCommand(Command.FONTSIZE, icon) {


    override fun initValuesDisplayTexts(): List<CharSequence> {
        return Arrays.asList(
                getHtmlSpanned(R.string.font_size_very_very_small),
                getHtmlSpanned(R.string.font_size_very_small),
                getHtmlSpanned(R.string.font_size_small),
                getHtmlSpanned(R.string.font_size_medium),
                getHtmlSpanned(R.string.font_size_large),
                getHtmlSpanned(R.string.font_size_very_large),
                getHtmlSpanned(R.string.font_size_very_very_large)
        )
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int) {
        val fontSize = position + 1 // position starts at 0, font sizes at 1

        executor.setFontSize(fontSize)
    }

}