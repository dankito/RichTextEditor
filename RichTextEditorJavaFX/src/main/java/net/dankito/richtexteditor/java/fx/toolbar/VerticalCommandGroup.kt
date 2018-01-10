package net.dankito.richtexteditor.java.fx.toolbar

import com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Labeled
import javafx.scene.control.ListCell
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import tornadofx.*


class VerticalCommandGroup(private val groupIcon: JavaFXIcon, val items: List<VerticalCommandGroupItem>)
    : ComboBox<VerticalCommandGroupItem>(FXCollections.observableList(items)) {

    companion object {
        private const val DisplayNodeIconSize = 18.0

        private const val ItemIconSize = 24.0
    }


    var commandInvoked: ((ToolbarCommand) -> Unit)? = null


    init {
        setupUi()
    }


    private fun setupUi() {
        prefWidth = 40.0

        setCellFactory {
            return@setCellFactory createCell()
        }

        this.selectionModel.selectedItemProperty().addListener { _, _, newValue -> itemSelected(newValue) }

        skinProperty().addListener { _, _, _ -> setGroupIcon() }
    }

    private fun createCell(): ListCell<VerticalCommandGroupItem> {
        return object : ListCell<VerticalCommandGroupItem>() {

            override fun updateItem(item: VerticalCommandGroupItem?, empty: Boolean) {
                super.updateItem(item, empty)

                if(item == null) {
                    text = ""
                    graphic = null
                }
                else {
                    text = item.title
                    setItemIcon(item)
                }
            }

            private fun setItemIcon(item: VerticalCommandGroupItem) {
                (item.command.icon as? JavaFXIcon)?.let { icon ->
                    if(graphic is ImageView) {
                        (graphic as? ImageView)?.image = Image(icon.url)
                    }
                    else {
                        val imageView = ImageView(Image(icon.url))
                        imageView.fitHeight = ItemIconSize
                        imageView.fitWidth = ItemIconSize

                        graphic = imageView
                    }
                }
            }

        }
    }

    private fun itemSelected(item: VerticalCommandGroupItem?) {
        item?.let {
            item.command.commandInvoked()

            commandInvoked?.invoke(item.command)

            runLater { // do it after method completed, otherwise due to a Bug in FX8 SelectionModel throws a null pointer exception, see https://stackoverflow.com/a/46810332
                selectionModel.clearSelection() // reset item selection otherwise same item couldn't be selected two times in a row
            }
        }

        runLater { setGroupIcon() } // also set icon after method completed otherwise skin would delete our icon again
    }

    private fun setGroupIcon() {
        ((skin as? ComboBoxBaseSkin<*>)?.displayNode as? Labeled)?.let { displayNode ->
            displayNode.contentDisplay = ContentDisplay.GRAPHIC_ONLY

            val imageView = ImageView(Image(groupIcon.url))
            imageView.fitHeight = DisplayNodeIconSize
            imageView.isPreserveRatio = true

            displayNode.graphic = imageView
        }
    }

}