package net.dankito.richtexteditor

import net.dankito.richtexteditor.command.Command
import net.dankito.richtexteditor.command.CommandState


data class EditorState(val didHtmlChange: Boolean, val html: String, var commandStates: MutableMap<Command, CommandState>) {

    constructor() : this(false, "", HashMap<Command, CommandState>()) // for Jackson

}