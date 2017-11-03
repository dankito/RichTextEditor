package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.RichTextEditor


abstract class SwitchColorOnOffCommand(private val offColor: Int, private var onColor: Int, command: Commands, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(),
                                       commandExecutedListener: (() -> Unit)? = null) : ColorCommand(offColor, command, iconResourceId, style, commandExecutedListener) {

    override fun currentColorChanged(color: Int) {
        super.currentColorChanged(color)

        if(color != offColor) {
            this.onColor = color
        }
    }

    override fun executeCommand(editor: RichTextEditor) {
        if(currentColor == offColor) {
            applyColor(editor, onColor)
        }
        else {
            applyColor(editor, offColor)
        }
    }


    abstract fun applyColor(editor: RichTextEditor, color: Int)

}