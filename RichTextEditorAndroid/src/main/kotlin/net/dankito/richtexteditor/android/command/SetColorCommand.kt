package net.dankito.richtexteditor.android.command

import android.app.Activity
import android.graphics.Color
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import net.dankito.richtexteditor.android.RichTextEditor


abstract class SetColorCommand(defaultColor: Int, command: Command, iconResourceId: Int, style: ToolbarCommandStyle = ToolbarCommandStyle(), commandExecutedListener: (() -> Unit)? = null)
    : ColorCommand(defaultColor, command, iconResourceId, style, commandExecutedListener) {


    override fun executeCommand(editor: RichTextEditor) {
        val currentColorWithoutAlpha = Color.rgb(Color.red(currentColor), Color.green(currentColor), Color.blue(currentColor)) // remove alpha value as html doesn't support alpha

        val colorPickerDialog = ColorPickerDialog.newBuilder()
                .setColor(currentColorWithoutAlpha)
                .setAllowPresets(true)
                .setAllowCustom(true)
                .setShowAlphaSlider(false)
//                .setDialogType(0) // 0 = PickerView, 1 = PresetsView, default is 1
                .create()

        colorPickerDialog.setColorPickerDialogListener(object : ColorPickerDialogListener {

            override fun onColorSelected(dialogId: Int, color: Int) {
                currentColorChanged(color)

                applySelectedColor(editor, color)
            }

            override fun onDialogDismissed(dialogId: Int) { }

        })

        colorPickerDialog.show((editor.context as Activity).fragmentManager, "")
    }


    protected abstract fun applySelectedColor(editor: RichTextEditor, color: Int)

}