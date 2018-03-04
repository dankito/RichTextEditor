package net.dankito.richtexteditor.java.fx.toolbar

import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.java.fx.command.SelectValueCommand


class SelectValueView(private val command: SelectValueCommand, private val commandInvoked: (ToolbarCommand) -> Unit) : ComboBox<String>() {

    private val values = FXCollections.observableArrayList<String>()

    private val itemSelectedListener = ChangeListener<String> { _, _, newValue ->
        itemSelected(command, newValue)
    }


    init {
        setupUi()

        command.addCommandValueChangedListener {  commandValueChanged(it) }
    }


    private fun setupUi() {
        prefWidth = 120.0

        setCellFactory {
            return@setCellFactory createCell()
        }

        values.setAll(command.getItemNames())
        this.items = values

        this.selectionModel.select(command.getDefaultItemName())

        addItemSelectedListener()
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


    private fun addItemSelectedListener() {
        this.selectionModel.selectedItemProperty().addListener(itemSelectedListener)
    }

    private fun removeItemSelectedListener() {
        this.selectionModel.selectedItemProperty().removeListener(itemSelectedListener)
    }

    private fun itemSelected(command: SelectValueCommand, newValue: String?) {
        newValue?.let {
            command.valueSelected(newValue)

            commandInvoked(command)
        }
    }

    private fun commandValueChanged(newValue: Any) {
        (newValue as? String)?.let {
            command.getItemIndexForCommandValue(newValue)?.let { valueIndex ->
                removeItemSelectedListener()

                try {
                    this.selectionModel.select(valueIndex)
                } catch(e: Exception) { }

                addItemSelectedListener()
            }
        }
    }


}