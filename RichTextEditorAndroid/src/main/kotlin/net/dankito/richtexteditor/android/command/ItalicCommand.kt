package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.ItalicCommandBase


open class ItalicCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_format_italic_white_48dp)) : ItalicCommandBase(icon)