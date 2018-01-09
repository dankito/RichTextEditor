package net.dankito.richtexteditor.java.fx.toolbar

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.java.fx.command.SelectValueCommand


class SelectValueView(private val command: SelectValueCommand, private val commandInvoked: (ToolbarCommand) -> Unit) : ComboBox<String>() {

    private val values = FXCollections.observableArrayList<String>()


    init {
        setupUi()
    }


    private fun setupUi() {
        prefWidth = 120.0

        setCellFactory {
            return@setCellFactory createCell()
        }

        values.setAll(command.getItemNames())
        this.items = values

        this.selectionModel.select(command.getDefaultItemName())

        this.selectionModel.selectedItemProperty().addListener { _, _, newValue -> itemSelected(command, newValue) }
    }

    private fun createCell(): ListCell<String> {
        return object : ListCell<String>() {

            override fun updateItem(item: String?, empty: Boolean) {
                super.updateItem(item, empty)

                text = item

                style = if (item != null) command.getItemStyle(item) else ""
            }

        }
    }

    private fun itemSelected(command: SelectValueCommand, newValue: String?) {
        newValue?.let {
            command.valueSelected(newValue)

            commandInvoked(command)
        }
    }

}