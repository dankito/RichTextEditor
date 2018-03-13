package net.dankito.richtexteditor.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import net.dankito.richtexteditor.android.R


class FullscreenWebViewOptionsBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    init {
        setupUi()
    }

    private fun setupUi() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.view_fullscreen_options_bar, this)


    }

}