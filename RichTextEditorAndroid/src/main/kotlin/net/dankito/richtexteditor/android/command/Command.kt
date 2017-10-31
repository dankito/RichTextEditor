package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.RichTextEditor


abstract class Command(val command: Commands,
              var iconResourceId: Int,
              var backgroundColorResourceId: Int = android.R.color.transparent,
              val widthDp: Int = DefaultWidthDp,
              val marginRightDp: Int = DefaultMarginRightDp,
              val paddingDp: Int = DefaultPaddingDp,
              val commandExecutedListener: (() -> Unit)? = null) {


    companion object {
        private const val DefaultWidthDp = 36

        private const val DefaultMarginRightDp = 4

        private const val DefaultPaddingDp = 4
    }


    var editor: RichTextEditor? = null


    fun commandInvoked() {
        editor?.let {
            executeCommand(it)
        }

        commandExecutedListener?.invoke()
    }

    abstract protected fun executeCommand(editor: RichTextEditor)

}