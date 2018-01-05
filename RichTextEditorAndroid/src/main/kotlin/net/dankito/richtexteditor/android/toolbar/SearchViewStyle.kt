package net.dankito.richtexteditor.android.toolbar

import android.graphics.Color
import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.ToolbarCommandStyle


data class SearchViewStyle(val commandStyle: ToolbarCommandStyle = ToolbarCommandStyle(),
                           val searchControlsBackgroundColor: Int = Color.TRANSPARENT, val searchFieldTextSize: Float = 14f,
                           val showSearchControlsIcon: Icon = AndroidIcon(R.drawable.ic_search_white_48dp),
                           val hideSearchControlsIcon: Icon = AndroidIcon(R.drawable.ic_clear_white_48dp),
                           val jumpToPreviousResultIcon: Icon = AndroidIcon(R.drawable.ic_arrow_up), val jumpToNextResultIcon: Icon = AndroidIcon(R.drawable.ic_arrow_down))