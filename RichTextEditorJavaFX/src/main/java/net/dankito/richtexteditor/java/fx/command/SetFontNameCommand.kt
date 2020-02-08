package net.dankito.richtexteditor.java.fx.command

import javafx.scene.text.Font
import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.command.CommandName
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class SetFontNameCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_font_download_black_36dp.png")) : SelectValueCommand(CommandName.FONTNAME, icon) {

    override fun getItemNames(): List<String> {
        return Font.getFamilies()
    }

    override fun getDefaultItemName(): String {
        return Font.getDefault().family
    }

    override fun getItemStyle(itemName: String): String {
        getIndexOfItem(itemName)?.let {
            return "-fx-font-family: \"${itemName.replace("\"", "\\\"")}\""
        }

        return ""
    }

    override fun valueSelected(executor: JavaScriptExecutorBase, position: Int, itemName: String) {
        executor.setFontName(itemName)
    }

}