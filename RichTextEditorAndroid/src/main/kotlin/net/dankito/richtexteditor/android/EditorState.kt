package net.dankito.richtexteditor.android

import net.dankito.richtexteditor.android.command.Command
import net.dankito.richtexteditor.android.command.CommandState


data class EditorState(val didHtmlChange: Boolean, val html: String, var commandStates: MutableMap<Command, CommandState>) {

    constructor() : this(false, "", HashMap<Command, CommandState>()) // for Jackson

}