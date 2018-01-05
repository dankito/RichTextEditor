package net.dankito.richtexteditor.android

import android.widget.ImageView
import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.CommandView


class AndroidCommandView(val view: ImageView) : CommandView() {

    override fun setIsEnabled(isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }

    override fun setBackgroundColor(color: Color) {
        view.setBackgroundColor(color.toInt())
    }

    override fun setTintColor(color: Color) {
        view.setColorFilter(color.toInt())
    }

}