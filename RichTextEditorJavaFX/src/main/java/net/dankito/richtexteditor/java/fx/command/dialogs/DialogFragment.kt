package net.dankito.richtexteditor.java.fx.command.dialogs

import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.Window
import tornadofx.*


abstract class DialogFragment : Fragment() {

    fun show(title: String? = null, iconUrl: String? = null, stageStyle: StageStyle = StageStyle.DECORATED, modality: Modality = Modality.NONE,
             owner: Window? = currentStage) : Stage {
        val dialogStage = Stage()

        dialogStage.title = title
        iconUrl?.let { dialogStage.icons.add(Image(it)) }
        owner?.let { dialogStage.initOwner(it) }

        dialogStage.initModality(modality)
        dialogStage.initStyle(stageStyle)

        val scene = Scene(this.root)

        dialogStage.scene = scene

        dialogStage.show()
        dialogStage.requestFocus()

        this.modalStage = dialogStage

        return dialogStage
    }

    fun closeDialog() {
        this.modalStage?.close()
    }

}