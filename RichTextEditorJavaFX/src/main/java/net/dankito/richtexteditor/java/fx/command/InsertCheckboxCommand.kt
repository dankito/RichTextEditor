package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.InsertCheckboxCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


class InsertCheckboxCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_insert_checkbox_black_36dp.png")) : InsertCheckboxCommandBase(icon)