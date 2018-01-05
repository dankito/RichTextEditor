package net.dankito.richtexteditor.android

import net.dankito.richtexteditor.Color


abstract class CommandView {

    abstract fun setIsEnabled(isEnabled: Boolean)

    abstract fun setBackgroundColor(color: Color)

    abstract fun setTintColor(color: Color)

}