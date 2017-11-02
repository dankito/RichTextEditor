package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor

class InsertCheckboxCommand : Command(Commands.INSERTCHECKBOX, R.drawable.ic_add_white_48dp) {


    override fun executeCommand(editor: RichTextEditor) {
        editor.insertCheckbox("")
    }

}