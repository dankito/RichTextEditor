package net.dankito.richtexteditor.java.fx.command

import net.dankito.utils.image.ImageReference
import net.dankito.richtexteditor.command.StrikeThroughCommandBase
import net.dankito.utils.javafx.ui.image.JavaFXImageReference


open class StrikeThroughCommand(icon: ImageReference = JavaFXImageReference.fromIconsResourceName("ic_format_strikethrough_black_36dp.png")) : StrikeThroughCommandBase(icon)