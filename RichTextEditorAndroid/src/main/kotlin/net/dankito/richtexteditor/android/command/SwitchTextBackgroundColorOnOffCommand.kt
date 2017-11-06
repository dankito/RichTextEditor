package net.dankito.richtexteditor.android.command

import android.graphics.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor


class SwitchTextBackgroundColorOnOffCommand: SwitchColorOnOffCommand(Color.TRANSPARENT, Color.YELLOW, Command.BACKCOLOR, R.drawable.ic_format_color_fill_white_48dp) {

    override fun applyColor(editor: RichTextEditor, color: Int) {
        editor.setTextBackgroundColor(color)
    }

    override fun currentColorChanged(color: Int) {
        super.currentColorChanged(color)

        commandView?.let { commandView ->
            commandView.setBackgroundColor(color)

            if(isExecutable && color == Color.WHITE && style.enabledTintColor == Color.WHITE) {
                if(style.isActivatedColor != Color.WHITE) {
                    commandView.setColorFilter(style.isActivatedColor)
                }
                else {
                    commandView.setColorFilter(Color.BLACK) // looks quite ugly to me
                }
            }
            else if(isExecutable && color == Color.BLACK && style.enabledTintColor == Color.BLACK) {
                commandView.setColorFilter(Color.WHITE)
            }
            else {
                setIconTintColorToExecutableState(commandView, isExecutable)
            }
        }
    }

}