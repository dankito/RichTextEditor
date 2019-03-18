package net.dankito.richtexteditor.android.toolbar

import android.graphics.Color
import net.dankito.utils.image.ImageReference
import net.dankito.utils.android.image.AndroidImageReference
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.command.ToolbarCommandStyle


data class SearchViewStyle(val commandStyle: ToolbarCommandStyle = ToolbarCommandStyle(),
                           val searchControlsBackgroundColor: Int = Color.TRANSPARENT, val searchFieldTextSize: Float = 14f,
                           val showSearchControlsIcon: ImageReference = AndroidImageReference(R.drawable.ic_search_white_48dp),
                           val hideSearchControlsIcon: ImageReference = AndroidImageReference(R.drawable.ic_clear_white_48dp),
                           val jumpToPreviousResultIcon: ImageReference = AndroidImageReference(R.drawable.ic_arrow_up), val jumpToNextResultIcon: ImageReference = AndroidImageReference(R.drawable.ic_arrow_down))