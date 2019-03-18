package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.SuperscriptCommandBase


class SuperscriptCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_format_superscript)) : SuperscriptCommandBase(icon)