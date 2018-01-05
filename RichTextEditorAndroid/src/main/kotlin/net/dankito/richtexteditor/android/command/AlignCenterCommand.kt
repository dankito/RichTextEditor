package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.JavaScriptExecutorBase
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.Command


class AlignCenterCommand(icon: Icon = AndroidIcon(R.drawable.ic_format_align_center_white_48dp)) : ActiveStateToolbarCommand(Command.JUSTIFYCENTER, icon) {

    override fun executeCommand(executor: JavaScriptExecutorBase) {
        executor.setJustifyCenter()
    }

}