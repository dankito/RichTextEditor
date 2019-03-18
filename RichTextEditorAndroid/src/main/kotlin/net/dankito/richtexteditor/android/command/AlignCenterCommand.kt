package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.AlignCenterCommandBase
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.utils.image.ImageReference


class AlignCenterCommand(icon: ImageReference = AndroidImageReference(R.drawable.ic_format_align_center_white_48dp)) : AlignCenterCommandBase(icon)