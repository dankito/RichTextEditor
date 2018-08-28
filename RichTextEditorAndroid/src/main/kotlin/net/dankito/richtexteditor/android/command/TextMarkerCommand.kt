package net.dankito.richtexteditor.android.command

import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R


open class TextMarkerCommand(icon: AndroidIcon = AndroidIcon(R.drawable.ic_marker_white_48dp))
    : SwitchTextBackgroundColorOnOffCommand(icon = icon, setOnColorToCurrentColor = false)