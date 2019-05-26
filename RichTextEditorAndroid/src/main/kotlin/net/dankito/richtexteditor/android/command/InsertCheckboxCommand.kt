package net.dankito.richtexteditor.android.command

import net.dankito.utils.image.ImageReference
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.InsertCheckboxCommandBase


open class InsertCheckboxCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_insert_checkbox)) : InsertCheckboxCommandBase(icon)