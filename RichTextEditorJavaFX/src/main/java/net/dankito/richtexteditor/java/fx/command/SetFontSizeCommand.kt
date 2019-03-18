package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.java.fx.localization.Localization
import net.dankito.utils.image.ImageReference
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


class SetFontSizeCommand(localization: Localization, icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_size_black_36dp.png"))
    : SelectValueCommand(CommandName.FONTSIZE, icon) {

    private val defaultFontSizeName = localization.getLocalizedString("font.size.medium")

    private val fontSizeNames = listOf(
            localization.getLocalizedString("font.size.very.very.small"),
            localization.getLocalizedString("font.size.very.small"),
            localization.getLocalizedString("font.size.small"),
            defaultFontSizeName,
            localization.getLocalizedString("font.size.large"),
            localization.getLocalizedString("font.size.very.large"),
            localization.getLocalizedString("font.size.very.very.large")
    )

    override fun getItemNames(): List<String> {
        return fontSizeNames
    }

    override fun getDefaultItemName(): String {
        return defaultFontSizeName
    }

    override fun getItemStyle(itemName: String): String {
        getIndexOfItem(itemName)?.let { index ->
            return "-fx-font-size:${getItemSize(index)}"
        }

        return ""
    }

    private fun getItemSize(index: Int): String {
        // see https://stackoverflow.com/a/5912671
        return when(index) {
            0 -> "9px"
            1 -> "10px"
            2 -> "13px"
            4 -> "18px"
            5 -> "24px"
            6 -> "32px"
            else -> "16px" // default: Medium (index: 3)
        }
    }


    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int, itemName: String) {
        val fontSize = position + 1 // position starts at 0, font sizes at 1

        executor.setFontSize(fontSize)
    }


    override fun getItemIndexForCommandValue(commandValue: String): Int? {
        try {
            return commandValue.toInt() - 1 // index starts at 0, font sizes at 1
        } catch(ignored: Exception) { } // commandValue could not be parsed to an int, should actually never happen

        return null
    }

}