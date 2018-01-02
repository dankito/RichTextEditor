package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout


class SearchView : LinearLayout {

    constructor(context: Context) : super(context) { initView(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initView(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView(context) }


    private lateinit var searchField: EditText


    private fun initView(context: Context) {
        this.orientation = HORIZONTAL

        searchField = EditText(context)
        addView(searchField)
    }

}