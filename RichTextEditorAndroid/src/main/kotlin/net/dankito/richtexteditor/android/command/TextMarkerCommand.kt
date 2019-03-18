package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.R
import net.dankito.utils.android.image.AndroidImageReference


open class TextMarkerCommand(icon: AndroidImageReference = AndroidImageReference(R.drawable.ic_marker_white_48dp))
    : SwitchTextBackgroundColorOnOffCommand(icon = icon, setOnColorToCurrentColor = false)