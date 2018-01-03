package net.dankito.richtexteditor.android.toolbar

import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.command.ToolbarCommandStyle


data class SearchViewStyle(val commandStyle: ToolbarCommandStyle = ToolbarCommandStyle(),
                           val searchFieldTextSize: Float = 14f,
                           val toggleSearchViewIconResourceId: Int = R.drawable.ic_search_white_48dp,
                           val jumpToPreviousResultIconResourceId: Int = R.drawable.ic_arrow_up, val jumpToNextResultIconResourceId: Int = R.drawable.ic_arrow_down)