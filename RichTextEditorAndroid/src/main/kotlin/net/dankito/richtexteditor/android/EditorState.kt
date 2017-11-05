package net.dankito.richtexteditor.android

import net.dankito.richtexteditor.android.command.CommandState
import net.dankito.richtexteditor.android.command.Command


data class EditorState(val didHtmlChange: Boolean, var commandStates: MutableMap<Command, CommandState>) {

    constructor() : this(false, HashMap<Command, CommandState>()) // for Jackson

}