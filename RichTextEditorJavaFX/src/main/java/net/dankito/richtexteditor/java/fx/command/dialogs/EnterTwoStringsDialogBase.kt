package net.dankito.richtexteditor.java.fx.command.dialogs

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.layout.Priority
import javafx.stage.StageStyle
import tornadofx.*


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


    abstract fun enteringStringsDone(valueOne: String, valueTwo: String)


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

                setOnAction { enteringStringsDone() }

                hboxConstraints {
                    hGrow = Priority.ALWAYS
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

            setOnAction { enteringStringsDone() }
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

                    setOnAction { closeDialog() }
                }
            }

            right {
                button(messages["ok"]) {
                    prefWidth = ButtonsWidth
                    useMaxHeight = true

                    setOnAction { enteringStringsDone() }
                }
            }
        }
    }


    fun show() {
        show(dialogTitle, stageStyle = StageStyle.UTILITY)
    }

    private fun enteringStringsDone() {
        enteringStringsDone(enteredStringOne.value, enteredStringTwo.value)

        closeDialog()
    }

}