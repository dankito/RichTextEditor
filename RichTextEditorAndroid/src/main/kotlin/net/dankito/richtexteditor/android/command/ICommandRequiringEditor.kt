package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.RichTextEditor


interface ICommandRequiringEditor {

    var editor: RichTextEditor?

}