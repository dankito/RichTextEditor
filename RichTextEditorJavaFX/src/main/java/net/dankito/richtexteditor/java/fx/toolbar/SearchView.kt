package net.dankito.richtexteditor.java.fx.toolbar

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.text.Font
import net.dankito.richtexteditor.java.fx.JavaFXJavaScriptExecutor
import net.dankito.richtexteditor.java.fx.RichTextEditor
import net.dankito.richtexteditor.java.fx.localization.Localization
import net.dankito.utils.javafx.ui.image.JavaFXImageReference
import tornadofx.*


class SearchView(private val searchViewStyle: SearchViewStyle, private val localization: Localization) : View() {


    var executor: JavaFXJavaScriptExecutor? = null

    var editor: RichTextEditor? = null


    private val searchText = SimpleStringProperty("")

    private val matchCase = SimpleBooleanProperty(false)

    private val enableButtonsPreviousNextSearchResult = SimpleBooleanProperty(false)


    init {
        searchText.addListener { _, _, _ -> search() }
    }


    override val root = hbox {
        alignment = Pos.CENTER_LEFT

        minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
        maxHeight = minHeight

        textfield(searchText) {
            font = Font.font(searchViewStyle.searchFieldFontSize)

            minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
            maxHeight = minHeight
            prefWidth = searchViewStyle.searchFieldWidth

            setTextFieldKeyboardShortcuts(this)
        }

        button("", createJumpToPreviousNextResultIcon(searchViewStyle.jumpToPreviousResultIcon)) {
            minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
            maxHeight = minHeight
            prefWidth = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()

            disableProperty().bind(enableButtonsPreviousNextSearchResult.not())

            action { search(false) }

            hboxConstraints {
                marginLeft = 2.0
            }
        }

        button("", createJumpToPreviousNextResultIcon(searchViewStyle.jumpToNextResultIcon)) {
            minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
            maxHeight = minHeight
            prefWidth = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()

            disableProperty().bind(enableButtonsPreviousNextSearchResult.not())

            action { search() }

            hboxConstraints {
                marginLeft = 2.0
                marginRight = 6.0
            }
        }

        checkbox(localization.getLocalizedString("search.match.case"), matchCase)
    }


    private fun setTextFieldKeyboardShortcuts(textField: TextField) {
        textField.setOnKeyReleased { event ->
            if (event.code == KeyCode.ESCAPE) {
                editor?.focusEditor()
            }
        }

        Platform.runLater {
            // on init scene is not set yet
            FX.primaryStage.scene?.accelerators?.put(KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN), Runnable {
                textField.requestFocus()
            })
        }
    }

    private fun createJumpToPreviousNextResultIcon(icon: JavaFXImageReference): Node {
        return ImageView(Image(icon.imageUrl, searchViewStyle.jumpToPreviousNextResultIconWidth, searchViewStyle.jumpToPreviousNextResultIconWidth, true, true))
    }


    private fun search(forward: Boolean = true) {
        executor?.page?.let { page ->
            enableButtonsPreviousNextSearchResult.value = page.find(searchText.value, forward, true, matchCase.value)
        }
    }

}