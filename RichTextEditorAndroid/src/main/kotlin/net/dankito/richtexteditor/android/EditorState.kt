package net.dankito.richtexteditor.android

import net.dankito.richtexteditor.android.command.CommandState
import net.dankito.richtexteditor.android.command.Commands


data class EditorState(val didHtmlChange: Boolean, var commandStates: MutableMap<Commands, CommandState>) {

    constructor() : this(false, HashMap<Commands, CommandState>()) // for Jackson

}