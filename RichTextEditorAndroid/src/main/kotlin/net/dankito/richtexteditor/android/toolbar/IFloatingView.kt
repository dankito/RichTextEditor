package net.dankito.richtexteditor.android.toolbar

import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.extensions.hideView
import net.dankito.richtexteditor.android.extensions.isVisible
import net.dankito.richtexteditor.android.extensions.showView
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.utils.android.ui.view.IHandlesBackButtonPress


interface IFloatingView : IHandlesBackButtonPress {

    var editor: RichTextEditor?

    var toolbar: EditorToolbar?

    var command: ToolbarCommand?

    var lastEditorHeight: Int

    var setMaxHeightOnNextMeasurement: Boolean

    var hasEditorHeightChanged: Boolean


    fun toggleShowView() {
        if(isVisible()) {
            hideView()
        }
        else {
            showView()
        }
    }

    override fun handlesBackButtonPress(): Boolean {
        if(isVisible()) {
            hideView()

            return true
        }

        return false
    }

}