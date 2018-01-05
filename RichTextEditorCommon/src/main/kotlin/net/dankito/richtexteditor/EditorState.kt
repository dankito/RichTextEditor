package net.dankito.richtexteditor

import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.CommandState


data class EditorState(val didHtmlChange: Boolean, val html: String, var commandStates: MutableMap<CommandName, CommandState>) {

    constructor() : this(false, "", HashMap<CommandName, CommandState>()) // for Jackson

}