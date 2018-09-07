package net.dankito.richtexteditor.android.toolbar

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import net.dankito.richtexteditor.android.AndroidIcon
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.util.StyleApplier
import net.dankito.utils.android.KeyboardUtils
import net.dankito.utils.android.extensions.*
import net.dankito.utils.android.ui.view.IHandlesBackButtonPress
import java.util.*
import kotlin.concurrent.schedule


class SearchView : LinearLayout, IHandlesBackButtonPress {

    companion object {
        const val SearchFieldMinWidthInDp = 100
        const val CountSearchResultsLabelMaxWidthInDp = 52
        const val ButtonDefaultWidthInDp = 40
        const val ButtonLeftMarginInDp = 4

        const val SearchControlsMaxWidthInDp = 215
    }


    constructor(context: Context) : super(context) { initView(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initView(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView(context) }


    var editor: RichTextEditor? = null
        set(value) {
            field = value

            this.webView = value
        }

    var webView: WebView? = null

    var searchViewExpandedListener: ((isExpanded: Boolean) -> Unit)? = null


    private lateinit var btnToggleSearchControlsVisibility: ImageButton

    lateinit var lytSearchControls: LinearLayout
        private set

    lateinit var searchField: EditText
        private set

    var isScrollingToSearchResult = false
        private set


    private lateinit var countSearchResultsLabel: TextView

    private lateinit var btnJumpToPreviousResult: ImageButton

    private lateinit var btnJumpToNextResult: ImageButton


    private val styleApplier = StyleApplier()

    private var style: SearchViewStyle? = null

    private val timerResetIsScrollingDueToSearchFieldTextChange = Timer()


    private fun initView(context: Context) {
        this.orientation = HORIZONTAL

        lytSearchControls = LinearLayout(context)
        lytSearchControls.orientation = HORIZONTAL
        lytSearchControls.visibility = View.GONE
        addView(lytSearchControls)

        lytSearchControls.layoutParams.width = getPixelSizeForDisplay(SearchControlsMaxWidthInDp)

        initSearchFieldAndLabel(context)

        initButtons(context)
    }

    private fun initSearchFieldAndLabel(context: Context) {
        searchField = EditText(context)
        lytSearchControls.addView(searchField, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT))

        searchField.minWidth = getPixelSizeForDisplay(SearchFieldMinWidthInDp)
        (searchField.layoutParams as LinearLayout.LayoutParams).weight = 2f

        searchField.inputType = InputType.TYPE_CLASS_TEXT
        searchField.addTextChangedListener(searchFieldTextWatcher)


        countSearchResultsLabel = TextView(context)
        lytSearchControls.addView(countSearchResultsLabel)

        countSearchResultsLabel.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        countSearchResultsLabel.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        countSearchResultsLabel.maxWidth = getPixelSizeForDisplay(CountSearchResultsLabelMaxWidthInDp)

        (countSearchResultsLabel.layoutParams as? LayoutParams)?.gravity = Gravity.CENTER
        countSearchResultsLabel.gravity = Gravity.CENTER
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            countSearchResultsLabel.textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        }

        onFindResultReceived(0, 0, true) // to set initial value
    }

    private fun initButtons(context: Context) {
        val buttonsLayoutParams = LayoutParams(getPixelSizeForDisplay(ButtonDefaultWidthInDp), ViewGroup.LayoutParams.MATCH_PARENT)
        buttonsLayoutParams.leftMargin = getPixelSizeForDisplay(ButtonLeftMarginInDp)

        btnJumpToPreviousResult = ImageButton(context)
        lytSearchControls.addView(btnJumpToPreviousResult, buttonsLayoutParams)

        btnJumpToPreviousResult.setOnClickListener { jumpToPreviousSearchResult() }


        btnJumpToNextResult = ImageButton(context)
        lytSearchControls.addView(btnJumpToNextResult, LayoutParams(buttonsLayoutParams as MarginLayoutParams))

        btnJumpToNextResult.setOnClickListener { jumpToNextSearchResult() }


        btnToggleSearchControlsVisibility = ImageButton(context)
        addView(btnToggleSearchControlsVisibility, LayoutParams(buttonsLayoutParams as MarginLayoutParams))

        btnToggleSearchControlsVisibility.setOnClickListener { toggleShowSearchView() }
    }


    fun applyStyle(style: SearchViewStyle) {
        this.style = style

        lytSearchControls.setBackgroundColor(style.searchControlsBackgroundColor)

        styleApplier.applyCommandStyle(style.showSearchControlsIcon, style.commandStyle, btnToggleSearchControlsVisibility)
        btnToggleSearchControlsVisibility.setColorFilter(style.commandStyle.enabledTintColor.toInt())

        styleApplier.applyCommandStyle(style.jumpToPreviousResultIcon, style.commandStyle, btnJumpToPreviousResult)
        btnJumpToPreviousResult.setColorFilter(style.commandStyle.enabledTintColor.toInt())
        if((style.jumpToPreviousResultIcon as? AndroidIcon)?.iconResourceId == R.drawable.ic_arrow_up) {
            btnJumpToPreviousResult.layoutParams.width = getLayoutSize(27)
        }

        styleApplier.applyCommandStyle(style.jumpToNextResultIcon, style.commandStyle, btnJumpToNextResult)
        btnJumpToNextResult.setColorFilter(style.commandStyle.enabledTintColor.toInt())
        if((style.jumpToNextResultIcon as? AndroidIcon)?.iconResourceId == R.drawable.ic_arrow_down) {
            btnJumpToNextResult.layoutParams.width = getLayoutSize(27)
        }


        searchField.textSize = style.searchFieldTextSize
        searchField.setTextColor(style.commandStyle.enabledTintColor.toInt())

        searchField.setBackgroundTintColor(style.commandStyle.enabledTintColor.toInt()) // sets line color at bottom of EditText
        searchField.clearCaretColor() // sets caret's color to text color (but caret is also smaller then)


        countSearchResultsLabel.setTextColor(style.commandStyle.enabledTintColor.toInt())
        val marginLeftRight = getLayoutSize(style.commandStyle.paddingDp)
        (countSearchResultsLabel.layoutParams as? LinearLayout.LayoutParams)?.setMargins(marginLeftRight, 0, marginLeftRight, 0)
    }


    open fun isExpanded(): Boolean {
        return lytSearchControls.isVisible()
    }

    private fun toggleShowSearchView() {
        if(isExpanded()) {
            collapse()
        }
        else {
            expand()
        }
    }

    fun expand() {
        (style?.hideSearchControlsIcon as? AndroidIcon)?.let { btnToggleSearchControlsVisibility.setImageResource(it.iconResourceId) }
        lytSearchControls.visibility = View.VISIBLE

        searchViewExpandedListener?.invoke(true)

        searchField.showKeyboard()
        searchInWebView(searchField.text.toString())

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { // if there are multiple SearchViews in layout, in this way it's ensured that last expanded SearchView sets FindListener
            webView?.setFindListener { activeMatchOrdinal, numberOfMatches, isDoneCounting -> onFindResultReceived(activeMatchOrdinal, numberOfMatches, isDoneCounting) }
        }
    }

    fun collapse() {
        if(editor != null) {
            if(editor?.isInFullscreenMode == true) {
                searchField.hideKeyboard()
            }
            else {
                editor?.focusEditorAndShowKeyboard()
                editor?.focusEditorAndShowKeyboardDelayed()
            }

            lytSearchControls.postDelayed({
                lytSearchControls.visibility = View.GONE
            }, 500)
        }
        else {
            searchField.hideKeyboard()
            lytSearchControls.visibility = View.GONE
        }

        (style?.showSearchControlsIcon as? AndroidIcon)?.let { btnToggleSearchControlsVisibility.setImageResource(it.iconResourceId) }
        clearSearchResults()
        searchViewExpandedListener?.invoke(false)
    }

    fun clearSearchResults() {
        webView?.clearMatches()
    }

    private fun searchInWebView(query: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView?.findAllAsync(query)
        }
        else {
            @Suppress("DEPRECATION")
            webView?.findAll(query)
        }
    }

    private fun jumpToPreviousSearchResult() {
        setIsScrollingToSearchResult()

        webView?.findNext(false)
    }

    private fun jumpToNextSearchResult() {
        setIsScrollingToSearchResult()

        webView?.findNext(true)
    }

    private fun onFindResultReceived(activeMatchOrdinal: Int, numberOfMatches: Int, doneCounting: Boolean) {
        if(doneCounting) {
            val currentMatch = if(numberOfMatches == 0) 0 else activeMatchOrdinal + 1
            countSearchResultsLabel.text = countSearchResultsLabel.context.getString(R.string.count_search_results_label, currentMatch, numberOfMatches)
        }
    }


    private fun setIsScrollingToSearchResult() {
        isScrollingToSearchResult = true

        timerResetIsScrollingDueToSearchFieldTextChange.schedule(350) { // reset after a while if onScrollChanged() has been called (due to search result was out of view) or not
            isScrollingToSearchResult = false // reset
        }
    }


    override fun handlesBackButtonPress(): Boolean {
        if(isExpanded()) {
            collapse()

            return true
        }

        return false
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        KeyboardUtils.addKeyboardToggleListener(context as Activity, softKeyboardToggleListener)
    }

    override fun onDetachedFromWindow() {
        KeyboardUtils.removeKeyboardToggleListener(softKeyboardToggleListener)

        super.onDetachedFromWindow()
    }

    private val softKeyboardToggleListener = { isKeyboardVisible: Boolean ->
        if(editor?.isInFullscreenMode == true) {
            if(isKeyboardVisible == false) {
                collapse()
            }
        }
    }



    private val searchFieldTextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            setIsScrollingToSearchResult()
        }

        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            searchInWebView(text.toString())
        }

        override fun afterTextChanged(s: Editable?) { }

    }

}