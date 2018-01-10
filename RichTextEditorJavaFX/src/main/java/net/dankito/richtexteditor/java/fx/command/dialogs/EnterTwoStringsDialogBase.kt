package net.dankito.richtexteditor.java.fx.command.dialogs

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File
import java.net.URL


abstract class EnterTwoStringsDialogBase(private val stringOneLabelText: String, private val stringTwoLabelText: String, private val dialogTitle: String)
    : DialogFragment() {

    companion object {
        private const val TextFieldsHeight = 32.0

        private const val LabelTextFieldVerticalMargin = 4.0
        private const val SectionsVerticalMargin = 12.0

        private const val ButtonsHeight = 40.0
        private const val ButtonsWidth = 200.0
    }


    protected val enteredStringOne = SimpleStringProperty("")

    protected val enteredStringTwo = SimpleStringProperty("")

    protected val isOkButtonEnabled = SimpleBooleanProperty(true)

    protected val isSelectLocalFileButtonVisible = SimpleBooleanProperty(false)


    abstract fun enteringStringsDone(valueOne: String, valueTwo: String)


    init {
        enteredStringOne.addListener { _, _, newValue -> determineIsOkButtonEnabled(newValue, enteredStringTwo.value) }
        enteredStringTwo.addListener { _, _, newValue -> determineIsOkButtonEnabled(enteredStringOne.value, newValue) }

        determineIsOkButtonEnabled(enteredStringOne.value, enteredStringTwo.value)
    }


    override val root = vbox {
        prefWidth = 450.0
        padding = Insets(4.0)

        label(stringOneLabelText) {
            vboxConstraints {
                marginBottom = LabelTextFieldVerticalMargin
            }
        }

        hbox {
            textfield(enteredStringOne) {
                prefHeight = TextFieldsHeight

                action { enteringStringsDone() }

                hboxConstraints {
                    hGrow = Priority.ALWAYS
                }
            }

            button("...") {
                prefHeight = TextFieldsHeight
                prefWidth = TextFieldsHeight

                visibleProperty().bind(isSelectLocalFileButtonVisible)
                managedProperty().bind(isSelectLocalFileButtonVisible)

                action { selectLocalFile() }

                hboxConstraints {
                    marginLeft = 6.0
                }
            }
        }

        label(stringTwoLabelText) {
            vboxConstraints {
                marginTop = SectionsVerticalMargin
                marginBottom = LabelTextFieldVerticalMargin
            }
        }

        textfield(enteredStringTwo) {
            prefHeight = TextFieldsHeight

            action { enteringStringsDone() }
        }

        borderpane {
            prefHeight = ButtonsHeight

            vboxConstraints {
                marginTop = 8.0
            }

            left {
                button(messages["cancel"]) {
                    prefWidth = ButtonsWidth
                    useMaxHeight = true

                    action { closeDialog() }
                }
            }

            right {
                button(messages["ok"]) {
                    prefWidth = ButtonsWidth
                    useMaxHeight = true

                    disableProperty().bind(isOkButtonEnabled.not())

                    action { enteringStringsDone() }
                }
            }
        }
    }


    fun show() {
        show(dialogTitle, stageStyle = StageStyle.UTILITY)
    }


    private fun selectLocalFile() {
        val fileChooser = FileChooser()
        fileChooser.title = getSelectLocalFileTitle()
        fileChooser.extensionFilters.addAll(getSelectLocalFileExtensionFilters())

        val file = File(enteredStringOne.value)
        val initialDirectory = if(file.isDirectory) file else if(file.parentFile?.isDirectory == true) file.parentFile else null
        initialDirectory?.let { fileChooser.initialDirectory = it }

        fileChooser.showOpenDialog(this.modalStage)?.let { selectedFile ->
            enteredStringOne.set(selectedFile.absolutePath)
        }
    }

    protected open fun getSelectLocalFileTitle(): String {
        return ""
    }

    protected open fun getSelectLocalFileExtensionFilters(): Collection<FileChooser.ExtensionFilter> {
        return emptyList()
    }


    private fun determineIsOkButtonEnabled(stringOne: String, stringTwo: String) {
        isOkButtonEnabled.set(isOkButtonEnabled(stringOne, stringTwo))
    }

    protected open fun isOkButtonEnabled(stringOne: String, stringTwo: String): Boolean {
        return true
    }

    protected fun isValidHttpUrl(string: String): Boolean {
        try {
            val url = URL(string)

            return url.host != null && url.protocol != null && url.protocol.startsWith("http")
        } catch(ignored: Exception) { }

        return false
    }

    private fun enteringStringsDone() {
        if(isOkButtonEnabled.value) {
            enteringStringsDone(enteredStringOne.value, enteredStringTwo.value)

            closeDialog()
        }
    }

}