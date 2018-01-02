package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.ToolbarCommandStyle
import net.dankito.richtexteditor.android.extensions.getPixelSizeForDisplay
import net.dankito.richtexteditor.android.extensions.hideKeyboard
import net.dankito.richtexteditor.android.extensions.showKeyboard


class SearchView : LinearLayout {

    companion object {
        const val SearchFieldDefaultWidthInDp = 150
        const val ButtonDefaultWidthInDp = 40
        const val ButtonLeftMarginInDp = 4
    }


    constructor(context: Context) : super(context) { initView(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initView(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView(context) }


    var editor: RichTextEditor? = null
        set(value) {
            field = value

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                value?.webView?.setFindListener { activeMatchOrdinal, numberOfMatches, isDoneCounting -> onFindResultReceived(activeMatchOrdinal, numberOfMatches, isDoneCounting) }
            }
        }

    private lateinit var btnToggleSearchControlsVisibility: ImageButton

    private lateinit var lytSearchControls: LinearLayout

    private lateinit var searchField: EditText

    private lateinit var btnJumpToPreviousResult: ImageButton

    private lateinit var btnJumpToNextResult: ImageButton


    private fun initView(context: Context) {
        this.orientation = HORIZONTAL

        lytSearchControls = LinearLayout(context)
        lytSearchControls.orientation = HORIZONTAL
        lytSearchControls.visibility = View.GONE
        addView(lytSearchControls)

        searchField = EditText(context)
        lytSearchControls.addView(searchField, LinearLayout.LayoutParams(getPixelSizeForDisplay(SearchFieldDefaultWidthInDp), ViewGroup.LayoutParams.MATCH_PARENT))

        searchField.addTextChangedListener(searchFieldTextWatcher)
        searchField.textSize = 14f


        val buttonsLayoutParams = LinearLayout.LayoutParams(getPixelSizeForDisplay(ButtonDefaultWidthInDp), ViewGroup.LayoutParams.MATCH_PARENT)
        buttonsLayoutParams.leftMargin = getPixelSizeForDisplay(ButtonLeftMarginInDp)

        btnJumpToPreviousResult = ImageButton(context)
        lytSearchControls.addView(btnJumpToPreviousResult, buttonsLayoutParams)

        btnJumpToPreviousResult.setOnClickListener { jumpToPreviousSearchResult() }

        btnJumpToNextResult = ImageButton(context)
        lytSearchControls.addView(btnJumpToNextResult, buttonsLayoutParams)

        btnJumpToNextResult.setOnClickListener { jumpToNextSearchResult() }

        btnToggleSearchControlsVisibility = ImageButton(context)
        addView(btnToggleSearchControlsVisibility, buttonsLayoutParams)

        btnToggleSearchControlsVisibility.setOnClickListener { toggleShowSearchView() }
    }


    fun applyStyle(toolbar: EditorToolbar, style: ToolbarCommandStyle, toggleSearchViewIconResourceId: Int,
                   jumpToPreviousResultIconResourceId: Int, jumpToNextResultIconResourceId: Int) {
        toolbar.applyCommandStyle(toggleSearchViewIconResourceId, style, btnToggleSearchControlsVisibility)
        btnToggleSearchControlsVisibility.setColorFilter(style.enabledTintColor)

        toolbar.applyCommandStyle(jumpToPreviousResultIconResourceId, style, btnJumpToPreviousResult)
        btnJumpToPreviousResult.setColorFilter(style.enabledTintColor)

        toolbar.applyCommandStyle(jumpToNextResultIconResourceId, style, btnJumpToNextResult)
        btnJumpToNextResult.setColorFilter(style.enabledTintColor)

        searchField.setTextColor(style.enabledTintColor)
    }


    private fun toggleShowSearchView() {
        if(lytSearchControls.visibility == View.GONE) {
            lytSearchControls.visibility = View.VISIBLE

            searchField.showKeyboard()
            searchInWebView(searchField.text.toString())
        }
        else {
            if(editor != null) {
                editor?.focusEditorAndShowKeyboard()
                editor?.focusEditorAndShowKeyboardDelayed()
            }
            else {
                searchField.hideKeyboard()
            }

            clearSearchResults()

            lytSearchControls.visibility = View.GONE
        }
    }

    private fun clearSearchResults() {
        editor?.webView?.clearMatches()
    }

    private fun searchInWebView(query: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            editor?.webView?.findAllAsync(query)
        }
        else {
            editor?.webView?.findAll(query)
        }
    }

    private fun jumpToPreviousSearchResult() {
        editor?.webView?.findNext(false)
    }

    private fun jumpToNextSearchResult() {
        editor?.webView?.findNext(true)
    }

    private fun onFindResultReceived(activeMatchOrdinal: Int, numberOfMatches: Int, doneCounting: Boolean) {

    }


    private val searchFieldTextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            searchInWebView(text.toString())
        }

        override fun afterTextChanged(s: Editable?) { }

    }

}