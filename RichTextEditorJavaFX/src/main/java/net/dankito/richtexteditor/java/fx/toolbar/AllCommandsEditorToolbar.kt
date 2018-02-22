package net.dankito.richtexteditor.java.fx.toolbar

import javafx.scene.control.ScrollPane
import net.dankito.richtexteditor.java.fx.command.*


class AllCommandsEditorToolbar : EditorToolbar() {


    init {
        root.minHeight = commandStyle.heightDp + 22.0 // to also have space for the scroll bar
        root.maxHeight = root.minHeight
        root.hbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS

        addCommand(SetTextFormatCommand(localization))
        addCommand(SetFontNameCommand())
        addCommand(SetFontSizeCommand(localization))

        addCommand(BoldCommand())
        addCommand(ItalicCommand())
        addCommand(UnderlineCommand())
        addCommand(StrikeThroughCommand())
        addCommand(SuperscriptCommand())
        addCommand(SubscriptCommand())
        addCommand(RemoveFormatCommand())

        addCommand(UndoCommand())
        addCommand(RedoCommand())

        addCommand(BlockQuoteCommand())
        addCommand(SwitchTextColorOnOffCommand())
        addCommand(SetTextColorCommand())
        addCommand(SwitchTextBackgroundColorOnOffCommand())
        addCommand(SetTextBackgroundColorCommand())

        addCommand(DecreaseIndentCommand())
        addCommand(IncreaseIndentCommand())

        addCommand(AlignLeftCommand())
        addCommand(AlignCenterCommand())
        addCommand(AlignRightCommand())
        addCommand(AlignJustifyCommand())

        addCommand(InsertBulletListCommand())
        addCommand(InsertNumberedListCommand())

        addCommand(InsertLinkCommand(this, localization))
        addCommand(InsertImageCommand(this, localization))
        addCommand(InsertCheckboxCommand())

        addSearchView()
    }

}