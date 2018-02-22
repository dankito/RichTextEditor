package net.dankito.richtexteditor.java.fx.toolbar

import javafx.scene.control.ScrollPane
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.command.*


class GroupedCommandsEditorToolbar : EditorToolbar() {

    init {
        root.minHeight = commandStyle.heightDp + GroupAdditionalHeight // so that groups also have space for padding at top and bottom
        root.maxHeight = root.minHeight
        root.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER


        addCommandInExtraGroup(SetTextFormatCommand(localization))
        addCommandInExtraGroup(SetFontNameCommand())
        addCommandInExtraGroup(SetFontSizeCommand(localization))


        val formatsGroup = CommandGroup()
        addGroup(formatsGroup)

        addCommand(BoldCommand(), formatsGroup)
        addCommand(ItalicCommand(), formatsGroup)
        addCommand(UnderlineCommand(), formatsGroup)

        val additionalFormatsVerticalGroup = VerticalCommandGroup(JavaFXIcon.fromResourceName("ic_format_superscript_black_36dp.png"), listOf(
                createItem(StrikeThroughCommand(), "command.title.strike.through"),
                createItem(SuperscriptCommand(), "command.title.superscript"),
                createItem(SubscriptCommand(), "command.title.subscript"),
                createItem(DecreaseIndentCommand(), "command.title.decrease.indent"),
                createItem(IncreaseIndentCommand(), "command.title.increase.indent"),
                createItem(RemoveFormatCommand(), "command.title.remove.format")
        ))
        addVerticalGroup(additionalFormatsVerticalGroup, formatsGroup)


        val undoGroup = CommandGroup()
        addGroup(undoGroup)

        addCommand(UndoCommand(), undoGroup)
        addCommand(RedoCommand(), undoGroup)

        val insertGroup = CommandGroup()
        addGroup(insertGroup)

        val alignmentVerticalGroup = VerticalCommandGroup(JavaFXIcon.fromResourceName("ic_format_align_left_black_36dp.png"), listOf(
                createItem(AlignLeftCommand(), "command.title.align.left"),
                createItem(AlignCenterCommand(), "command.title.align.center"),
                createItem(AlignRightCommand(), "command.title.align.right"),
                createItem(AlignJustifyCommand(), "command.title.align.justify")
        ))
        addVerticalGroup(alignmentVerticalGroup, insertGroup)

        val insertVerticalGroup = VerticalCommandGroup(JavaFXIcon.fromResourceName("ic_insert_photo_black_36dp.png"), listOf(
                createItem(InsertBulletListCommand(), "command.title.insert.bullet.list"),
                createItem(InsertNumberedListCommand(), "command.title.insert.numbered.list"),
                createItem(InsertLinkCommand(this, localization), "command.title.insert.link"),
                createItem(InsertImageCommand(this, localization), "command.title.insert.image"),
                createItem(InsertCheckboxCommand(), "command.title.insert.checkbox")
        ))
        addVerticalGroup(insertVerticalGroup, insertGroup)

        addSearchViewInExtraGroup()
    }

    private fun createItem(command: ToolbarCommand, titleResourceKey: String): VerticalCommandGroupItem {
        return VerticalCommandGroupItem(command, localization.getLocalizedString(titleResourceKey))
    }

}