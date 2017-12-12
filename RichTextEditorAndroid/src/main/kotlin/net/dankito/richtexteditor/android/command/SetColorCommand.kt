package net.dankito.richtexteditor.android.command

import android.app.Activity
import android.graphics.Color
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.extensions.hideKeyboard
import net.dankito.richtexteditor.android.util.KeyboardState


abstract class SetColorCommand(defaultColor: Int, showColorInCommandView: Boolean = true, command: Command, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(defaultColor, showColorInCommandView, command, iconResourceId, style, commandExecutedListener) {


    override fun executeCommand(editor: RichTextEditor) {
        val currentColorWithoutAlpha = Color.rgb(Color.red(currentColor), Color.green(currentColor), Color.blue(currentColor)) // remove alpha value as html doesn't support alpha

        val wasKeyboardVisible = getIsKeyboardVisibleAndCloseIfSo(editor)

        val colorPickerDialog = ColorPickerDialog.newBuilder()
                .setColor(currentColorWithoutAlpha)
                .setShowAlphaSlider(false)
//                .setDialogType(0) // 0 = PickerView, 1 = PresetsView, default is 1
                .create()

        colorPickerDialog.setColorPickerDialogListener(object : ColorPickerDialogListener {

            override fun onColorSelected(dialogId: Int, color: Int) {
                currentColorChanged(color)

                applySelectedColor(editor, color)
            }

            override fun onDialogDismissed(dialogId: Int) {
                if(wasKeyboardVisible) {
                    editor.focusEditorAndShowKeyboardDelayed(alsoCallJavaScriptFocusFunction = false) // don't call JavaScript focus() function has this would change state and just selected color would therefore get lost
                }
            }

        })

        colorPickerDialog.show((editor.context as Activity).fragmentManager, "")
    }

    private fun getIsKeyboardVisibleAndCloseIfSo(editor: RichTextEditor): Boolean {
        val isVisible = KeyboardState.isKeyboardVisible

        if(isVisible) {
            editor.hideKeyboard()
        }

        return isVisible
    }


    protected abstract fun applySelectedColor(editor: RichTextEditor, color: Int)

}