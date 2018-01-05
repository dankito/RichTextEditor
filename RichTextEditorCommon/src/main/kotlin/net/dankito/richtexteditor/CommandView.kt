package net.dankito.richtexteditor


abstract class CommandView {

    abstract fun setIsEnabled(isEnabled: Boolean)

    abstract fun setBackgroundColor(color: Color)

    abstract fun setTintColor(color: Color)

}