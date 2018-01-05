package net.dankito.richtexteditor.android.command

import android.app.Activity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.extensions.hideKeyboard
import net.dankito.richtexteditor.android.util.KeyboardState
import net.dankito.richtexteditor.command.CommandName


abstract class SetColorCommand(defaultColor: Color, showColorInCommandView: Boolean = true, command: CommandName, icon: Icon, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(defaultColor, showColorInCommandView, command, icon, style, commandExecutedListener), ICommandRequiringEditor {

    override var editor: RichTextEditor? = null


    override fun executeCommand(executor: JavaScriptExecutorBase) {
        val currentColorWithoutAlpha = Color(currentColor.red, currentColor.green, currentColor.blue) // remove alpha value as html doesn't support alpha

        val wasKeyboardVisible = getIsKeyboardVisibleAndCloseIfSo(editor!!)

        val colorPickerDialog = ColorPickerDialog.newBuilder()
                .setColor(currentColorWithoutAlpha.toInt())
                .setShowAlphaSlider(false)
//                .setDialogType(0) // 0 = PickerView, 1 = PresetsView, default is 1
                .create()

        colorPickerDialog.setColorPickerDialogListener(object : ColorPickerDialogListener {

            override fun onColorSelected(dialogId: Int, color: Int) {
                val convertedColor = Color(android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color), android.graphics.Color.alpha(color))
                currentColorChanged(convertedColor)

                applySelectedColor(executor, convertedColor)
            }

            override fun onDialogDismissed(dialogId: Int) {
                if(wasKeyboardVisible) {
                    editor!!.focusEditorAndShowKeyboardDelayed(alsoCallJavaScriptFocusFunction = false) // don't call JavaScript focus() function has this would change state and just selected color would therefore get lost
                }
            }

        })

        colorPickerDialog.show((editor!!.context as Activity).fragmentManager, "")
    }

    private fun getIsKeyboardVisibleAndCloseIfSo(editor: RichTextEditor): Boolean {
        val isVisible = KeyboardState.isKeyboardVisible

        if(isVisible) {
            editor.hideKeyboard()
        }

        return isVisible
    }


    protected abstract fun applySelectedColor(executor: JavaScriptExecutorBase, color: Color)

}