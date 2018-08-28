package net.dankito.richtexteditor.android

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.CommandView
import net.dankito.richtexteditor.android.toolbar.SelectValueWithPreviewView


class AndroidCommandView(val view: View) : CommandView() {

    override var appliedTintColor: Color = Color.Transparent


    override fun setIsEnabled(isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }

    override fun setBackgroundColor(color: Color) {
        view.setBackgroundColor(color.toInt())
    }

    override fun getParentBackgroundColor(): Color? {
        var parent: View? = view.parent as? View

        while(parent != null) {
            (parent.background as? ColorDrawable)?.let { colorDrawable ->
                return Color.fromArgb(colorDrawable.color)
            }

            parent = parent.parent as? View
        }

        return null
    }

    override fun setTintColor(color: Color) {
        appliedTintColor = color

        if(view is ImageView) {
            view.setColorFilter(color.toInt())
        }
        else if(view is SelectValueWithPreviewView) {
            view.setTintColor(color)
        }
    }

}