package net.dankito.richtexteditor.android

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import kotlinx.android.synthetic.main.view_fullscreen_options_bar.view.*
import net.dankito.richtexteditor.android.extensions.getColorFromResource
import net.dankito.richtexteditor.android.extensions.showKeyboard
import net.dankito.richtexteditor.android.toolbar.SearchView
import net.dankito.richtexteditor.android.toolbar.SearchViewStyle
import net.dankito.richtexteditor.android.util.OnSwipeTouchListener
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import java.util.*


/**
 * Actually only needed for may other app DeepThought.
 * There you can either view html in fullscreen without keyboard and basic editing function for selected text only or
 * edit it in non fullscreen with keyboard enabled and all editing functions available through editorToolbar.
 *
 * On scroll down fires a listener to enter full screen mode, on scroll up fires the same listener to leave full screen mode.
 */
open class FullscreenWebView : WebView {

    enum class FullscreenMode {
        Enter,
        Leave
    }

    enum class DisplayMode {
        Viewing,
        Editing
    }


    companion object {
        private const val DefaultScrollDownDifferenceYThreshold = 3
        private const val DefaultScrollUpDifferenceYThreshold = -10

        private const val AfterTogglingNotHandleScrollEventsForMillis = 500

        private const val SCROLL_X_BUNDLE_NAME = "SCROLL_X"
        private const val SCROLL_Y_BUNDLE_NAME = "SCROLL_Y"


        private const val NON_FULLSCREEN_MODE_SYSTEM_UI_FLAGS = 0
        private val FULLSCREEN_MODE_SYSTEM_UI_FLAGS: Int


        init {
            FULLSCREEN_MODE_SYSTEM_UI_FLAGS = createFullscreenModeSystemUiFlags()
        }

        private fun createFullscreenModeSystemUiFlags(): Int {
            // see https://developer.android.com/training/system-ui/immersive.html
            var flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                // even thought View.SYSTEM_UI_FLAG_FULLSCREEN is also available from SDK 16 and above, to my experience it doesn't work reliable (at least not on Android 4.1)
                flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                flags = flags or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
            }

            return flags
        }
    }


    var isInViewingMode = false
        private set

    var isInFullscreenMode = false
        private set

    var scrollUpDifferenceYThreshold = DefaultScrollUpDifferenceYThreshold
    var scrollDownDifferenceYThreshold = DefaultScrollDownDifferenceYThreshold

    var changeFullscreenModeListener: ((FullscreenMode) -> Unit)? = null

    var changeDisplayModeListener: ((DisplayMode) -> Unit)? = null

    private var leftFullscreenCallback: (() -> Unit)? = null

    var singleTapListener: ((isInFullscreen: Boolean) -> Unit)? = null

    var doubleTapListener: ((isInFullscreen: Boolean) -> Unit)? = null

    var swipeListener: ((isInFullscreen: Boolean, OnSwipeTouchListener.SwipeDirection) -> Unit)? = null

    /**
     * Should return true if Android's url loading should be disabled
     */
    var elementClickedListener: ((elementType: Int) -> Boolean)? = null


    private var hasReachedEnd = false

    private var disableScrolling = false

    private var lastOnScrollFullscreenModeTogglingTimestamp: Date? = null

    private lateinit var swipeTouchListener: OnSwipeTouchListener


    private var editorToolbar: View? = null

    private var optionsBar: View? = null

    private var searchView: SearchView? = null

    private var isSearchViewVisible = false

    private var wasSearchFocusedFocusedBeforeScrolling = false

    private var checkIfScrollingStoppedTimer = Timer()

    private var checkIfScrollingStoppedTimerTask: TimerTask? = null

    private var scrollPositionToRestore: Point? = null

    private var isRestoringScrollPosition = false


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        setupUI()
    }

    private fun setupUI() {
        swipeTouchListener = OnSwipeTouchListener(context) { handleWebViewSwipe(it) }

        swipeTouchListener.singleTapListener = { handleWebViewSingleTap() }
        swipeTouchListener.doubleTapListener = { handleWebViewDoubleTap() }
    }


    fun setEditorToolbarAndOptionsBar(editorToolbar: View, optionsBar: View? = null) {
        this.editorToolbar = editorToolbar
        this.optionsBar = optionsBar
        this.searchView = optionsBar?.optionsBarSearchView

        optionsBar?.btnLeaveFullscreen?.setOnClickListener { leaveFullscreenMode() }

        searchView?.searchField?.textSize = 16f
        val searchControlsBackground = context.getColorFromResource(R.color.richtexteditor_fullscreen_options_bar_search_view_background)
        searchView?.applyStyle(SearchViewStyle(ToolbarCommandStyle(), searchControlsBackground, 16f))
        searchView?.editor = this as? RichTextEditor

        searchView?.searchViewExpandedListener = { isExpanded ->
            if(isExpanded) {
                isSearchViewVisible = true
            }
            else {
                isSearchViewVisible = false

                this.systemUiVisibility = FULLSCREEN_MODE_SYSTEM_UI_FLAGS
            }
        }
    }


    override fun onWindowSystemUiVisibilityChanged(flags: Int) {
        if(flags == 0 && isSearchViewVisible == false) {
            isInFullscreenMode = false // otherwise isInFullscreenMode stays true and full screen mode isn't entered anymore on resume
        }

        // as immersive fullscreen is only available for KitKat and above leave immersive fullscreen mode by swiping from screen top or bottom is also only available on these  devices
        if(flags != FULLSCREEN_MODE_SYSTEM_UI_FLAGS && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(isSearchViewVisible == false) {
                leaveFullscreenMode()

                leftFullscreenCallback?.invoke()
                leftFullscreenCallback = null
            }
            else {
                searchView?.postDelayed({
                    if(flags == 0) { // user tapped e.g. on searchField at bottom of screen when keyboard is hidden -> go back to fullscreen
                        searchView?.searchField?.showKeyboard()
                    }

                    this.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                }, 250)
            }
        }

        super.onWindowSystemUiVisibilityChanged(flags)
    }

    /**
     * WebView doesn't fire click event, so we had to implement this our self
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        swipeTouchListener.onTouch(this, event)

        if(event.action == MotionEvent.ACTION_UP && elementClickedListener != null) {
            hitTestResult?.let { hitResult ->
                val type = hitResult.type

                elementClickedListener?.let {  // this is bad: in most cases type is UNKNOWN, even though clicked on images etc. -> we cannot determine if user clicked an element or simply the background
                    if(it.invoke(type)) { // only abort touch handling if elementClickedListener really handled touch
                        return true
                    }
                }
            }
        }

        if(disableScrolling) { // if both taps of a double tap weren't exactly on the same place may a large scroll occur after transition to fullscreen / not-fullscreen mode -> disable scrolling during this time
            return event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_UP // somehow we also have to catch the last ACTION_UP as otherwise text gets selected
        }

        return super.onTouchEvent(event)
    }

    private fun handleWebViewSingleTap() {
        val type = hitTestResult?.type

        // leave the functionality for clicking on links, phone numbers, geo coordinates, ... Only go to fullscreen mode when clicked somewhere else in the WebView or on an image
        if(type == null || type == WebView.HitTestResult.UNKNOWN_TYPE || type == WebView.HitTestResult.IMAGE_TYPE) {
            singleTapListener?.invoke(isInFullscreenMode)
        }
        else if(type == WebView.HitTestResult.EDIT_TEXT_TYPE && isInViewingMode && isInFullscreenMode == false) {
            enterEditingMode()
        }
    }

    private fun handleWebViewDoubleTap() {
        disableScrolling = true // otherwise double tap may triggers a large scroll

        if(isInViewingMode) {
            if(isInFullscreenMode) {
                leaveFullscreenMode()
            }
            else {
                checkShouldEnterFullscreenMode()
            }
        }

        doubleTapListener?.invoke(isInFullscreenMode)

        postDelayed({
            disableScrolling = false
        }, 300)
    }

    private fun handleWebViewSwipe(swipeDirection: OnSwipeTouchListener.SwipeDirection) {
        swipeListener?.invoke(isInFullscreenMode, swipeDirection)
    }


    override fun onScrollChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)

        if(isInViewingMode == false || searchView?.isScrollingToSearchResult == true) { // enter fullscreen on scroll is only enabled in viewing mode; filter out non-user scrolls
            return
        }

        if(hasFullscreenModeToggledShortlyBefore()) {
            return // when toggling reader mode there's a huge jump in scroll difference due to displaying additional / hiding controls -> filter out these events shortly after  entering/leaving reader mode
        }

        val differenceY = scrollY - oldScrollY

        if(isInFullscreenMode == false) {
            checkShouldEnterFullscreenMode(differenceY)
        }

        val tolerance = computeVerticalScrollExtent() / 10
        this.hasReachedEnd = scrollY >= computeVerticalScrollRange() - computeVerticalScrollExtent() - tolerance

        wasSearchFocusedFocusedBeforeScrolling = wasSearchFocusedFocusedBeforeScrolling || searchView?.searchField?.isFocused == true // wasSearchFocusedFocusedBeforeScrolling || : otherwise in scroll events short after hiding options bar wasSearchFocusedFocusedBeforeScrolling would get set from true to false
        hideOptionsBarOnUiThread()
        startCheckIfScrollingStopped()
    }

    private fun checkShouldEnterFullscreenMode(differenceY: Int) {
        if((differenceY > scrollDownDifferenceYThreshold || differenceY < scrollUpDifferenceYThreshold) && isRestoringScrollPosition == false) {
            checkShouldEnterFullscreenMode()
        }
    }

    private fun checkShouldEnterFullscreenMode() {
        if(isInViewingMode) {
            enterFullscreenMode()
        }
    }

    private fun startCheckIfScrollingStopped() {
        checkIfScrollingStoppedTimerTask?.cancel()

        checkIfScrollingStoppedTimerTask = object: TimerTask() {
            override fun run() {
                if(isInFullscreenMode) {
                    showOptionsBar()
                }
            }
        }

        checkIfScrollingStoppedTimer.schedule(checkIfScrollingStoppedTimerTask, 300)
    }

    private fun hideOptionsBarOnUiThread() {
        optionsBar?.visibility = View.GONE
    }

    private fun showOptionsBar() {
        (context as? Activity)?.let { activity ->
            activity.runOnUiThread {
                showOptionsBarOnUiThread()
            }
        }
    }

    private fun showOptionsBarOnUiThread() {
        optionsBar?.visibility = View.VISIBLE

        if(wasSearchFocusedFocusedBeforeScrolling) {
            searchView?.requestFocus()

            wasSearchFocusedFocusedBeforeScrolling = false // reset value to not focus accidentally again
        }
    }


    open fun enterEditingMode() {
        isInViewingMode = false

        this.editorToolbar?.visibility = View.VISIBLE
        this.optionsBar?.visibility = View.GONE

        this.isFocusable = true
        this.isFocusableInTouchMode = true

        this.showKeyboard()

        changeDisplayModeListener?.invoke(DisplayMode.Editing)
    }

    open fun enterViewingMode() {
        isInViewingMode = true

        this.editorToolbar?.visibility = View.GONE

        this.isFocusable = false
        this.isFocusableInTouchMode = false

        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)

        changeDisplayModeListener?.invoke(DisplayMode.Viewing)
    }


    private fun enterFullscreenMode() {
        isInFullscreenMode = true
        updateLastOnScrollFullscreenModeTogglingTimestamp()
        showOptionsBar()
        requestFocus() // e.g. currently focused view shows keyboard -> keyboard (or other elements) still would be displayed

        changeFullscreenModeListener?.invoke(FullscreenMode.Enter)

        this.systemUiVisibility = FULLSCREEN_MODE_SYSTEM_UI_FLAGS
    }


    fun leaveFullscreenModeAndWaitTillLeft(leftFullscreenCallback: () -> Unit) {
        this.leftFullscreenCallback = leftFullscreenCallback

        leaveFullscreenMode()
    }

    private fun leaveFullscreenMode() {
        isInFullscreenMode = false
        updateLastOnScrollFullscreenModeTogglingTimestamp()
        hideOptionsBarOnUiThread()
        searchView?.hideSearchControls()

        changeFullscreenModeListener?.invoke(FullscreenMode.Leave)

        this.systemUiVisibility = NON_FULLSCREEN_MODE_SYSTEM_UI_FLAGS

        if(hasReachedEnd) {
            scrollToEndDelayed()
        }
    }


    private fun hasFullscreenModeToggledShortlyBefore(): Boolean {
        return Date().time - (lastOnScrollFullscreenModeTogglingTimestamp?.time ?: 0) < AfterTogglingNotHandleScrollEventsForMillis
    }


    private fun scrollToEndDelayed() {
        postDelayed({
            scrollToEnd()
        }, 50)
    }

    private fun scrollToEnd() {
        updateLastOnScrollFullscreenModeTogglingTimestamp() // we also have to set lastOnScrollFullscreenModeTogglingTimestamp as otherwise scrolling may is large enough to re-enter fullscreen mode
        scrollY = computeVerticalScrollRange() - computeVerticalScrollExtent()
        updateLastOnScrollFullscreenModeTogglingTimestamp() // to be on the safe side
    }


    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SCROLL_X_BUNDLE_NAME, scrollX)
        outState.putInt(SCROLL_Y_BUNDLE_NAME, scrollY)
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        scrollPositionToRestore = Point(savedInstanceState.getInt(SCROLL_X_BUNDLE_NAME), savedInstanceState.getInt(SCROLL_Y_BUNDLE_NAME))

        mayRestoreScrollPosition()
    }

    fun activityPaused() {
        if(scrollX != 0 || scrollY != 0) {
            scrollPositionToRestore = Point(scrollX, scrollY) // onRestoreState() sometimes simply doesn't get call, but scrollY gets nevertheless re-set to 0
        }

        checkIfScrollingStoppedTimerTask?.cancel() // otherwise may causes a memory leak

        if(isInFullscreenMode) {
            leaveFullscreenModeAndWaitTillLeft { }
        }
    }

    fun activityResumed() {
        mayRestoreScrollPosition()
    }


    /*      Ensure that a scroll due to loadData() doesn't toggle Fullscreen        */

    override fun loadData(data: String?, mimeType: String?, encoding: String?) {
        updateLastOnScrollFullscreenModeTogglingTimestamp()
        super.loadData(data, mimeType, encoding)

        mayRestoreScrollPosition()
    }

    override fun loadDataWithBaseURL(baseUrl: String?, data: String?, mimeType: String?, encoding: String?, historyUrl: String?) {
        updateLastOnScrollFullscreenModeTogglingTimestamp()
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)

        mayRestoreScrollPosition()
    }

    override fun loadUrl(url: String?) {
        updateLastOnScrollFullscreenModeTogglingTimestamp()
        super.loadUrl(url)

        mayRestoreScrollPosition()
    }

    override fun loadUrl(url: String?, additionalHttpHeaders: MutableMap<String, String>?) {
        updateLastOnScrollFullscreenModeTogglingTimestamp()
        super.loadUrl(url, additionalHttpHeaders)

        mayRestoreScrollPosition()
    }

    private fun updateLastOnScrollFullscreenModeTogglingTimestamp() {
        lastOnScrollFullscreenModeTogglingTimestamp = Date()
    }

    private fun mayRestoreScrollPosition() {
        scrollPositionToRestore?.let { scrollPositionToRestore -> // restore WebView's scroll position before fullscreen has been left in onPause()
            if(computeVerticalScrollRange() > scrollPositionToRestore.y) {
                scrollX = scrollPositionToRestore.x
                scrollY = scrollPositionToRestore.y
            }

            postDelayed({
                isRestoringScrollPosition = true

                if(computeVerticalScrollRange() > scrollPositionToRestore.y) { // don't know why we have to do it delayed in order to work
                    scrollX = scrollPositionToRestore.x
                    scrollY = scrollPositionToRestore.y
                }

                this.scrollPositionToRestore = null // TODO: really setting back to null without knowing if it got applied?
                isRestoringScrollPosition = false
            }, 500)
        }
    }

}
