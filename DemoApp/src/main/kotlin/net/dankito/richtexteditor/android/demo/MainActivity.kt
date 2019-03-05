package net.dankito.richtexteditor.android.demo

import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.dankito.readability4j.extended.Readability4JExtended
import net.dankito.richtexteditor.android.FullscreenWebView
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.demo.dialogs.AddHtmlFromWebPageDialog
import net.dankito.richtexteditor.android.theme.ActivityThemes
import net.dankito.richtexteditor.android.toolbar.AllCommandsEditorToolbar
import net.dankito.richtexteditor.android.toolbar.EditorToolbar
import net.dankito.richtexteditor.android.toolbar.GroupedCommandsEditorToolbar
import net.dankito.richtexteditor.model.DownloadImageConfig
import net.dankito.richtexteditor.model.DownloadImageUiSetting
import net.dankito.utils.android.permissions.PermissionsService
import net.dankito.utils.android.ui.activities.ThemeableActivity
import net.dankito.utils.android.ui.theme.Theme
import net.dankito.utils.web.client.OkHttpWebClient
import net.dankito.utils.web.client.RequestParameters
import java.io.File


class MainActivity : ThemeableActivity() {

    companion object {

        private const val PlaceToolbarAtTopPreferenceKeySuffix = "place_toolbar_at_top"

        private const val ShowToolbarInlinePreferenceKeySuffix = "show_toolbar_inline"

    }

    enum class ToolbarPlacement {
        Top,
        Bottom
    }

    enum class ToolbarAppearance {
        Inline,
        Grouped
    }


    private lateinit var editor: RichTextEditor

    private lateinit var topInlineToolbar: AllCommandsEditorToolbar

    private lateinit var topGroupedCommandsToolbar: GroupedCommandsEditorToolbar

    private lateinit var bottomInlineToolbar: AllCommandsEditorToolbar

    private lateinit var bottomGroupedCommandsToolbar: GroupedCommandsEditorToolbar


    private var toolbarPlacement = ToolbarPlacement.Bottom

    private var toolbarAppearance = ToolbarAppearance.Grouped

    private val permissionsService = PermissionsService(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        editor = findViewById(R.id.editor) as RichTextEditor

        // this is needed if you like to insert images so that the user gets asked for permission to access external storage if needed
        // see also onRequestPermissionsResult() below
        editor.permissionsService = permissionsService

        topInlineToolbar = findViewById(R.id.topInlineToolbar) as AllCommandsEditorToolbar
        topInlineToolbar.editor = editor

        topGroupedCommandsToolbar = findViewById(R.id.topGroupedCommandsToolbar) as GroupedCommandsEditorToolbar
        topGroupedCommandsToolbar.editor = editor

        bottomInlineToolbar = findViewById(R.id.bottomInlineToolbar) as AllCommandsEditorToolbar
        bottomInlineToolbar.editor = editor

        bottomGroupedCommandsToolbar = findViewById(R.id.bottomGroupedCommandsToolbar) as GroupedCommandsEditorToolbar
        bottomGroupedCommandsToolbar.editor = editor

        editor.setEditorFontSize(20)
        editor.setPadding((4 * resources.displayMetrics.density).toInt())

        // some properties you also can set on editor
//        editor.setEditorBackgroundColor(Color.YELLOW)
//        editor.setEditorFontColor(Color.MAGENTA)
//        editor.setEditorFontFamily("cursive")

        // show keyboard right at start up
//        editor.focusEditorAndShowKeyboardDelayed()

        editor.downloadImageConfig = DownloadImageConfig(DownloadImageUiSetting.AllowSelectDownloadFolderInCode,
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "downloaded_images"))

        // semi transparent options bar shown when entering fullscreen in viewing mode
        fullscreenOptionsBar.showMarkSelectedTextButton(editor)
        setToolbarAppearanceAndPlacement()
        editor.changeFullscreenModeListener = { mode -> fullScreenModeChanged(mode) }

        editor.enterEditingMode()
        // show editor in viewing, not editing, mode. Tap on editor switches back to editing mode then.
        // But to be able to enter fullscreen mode you need to set HTML that fills at least on display page.
//        editor.enterViewingMode()

        if (getBooleanPreferenceValue(getPlaceToolbarAtTopPreferenceKey(), false)) {
            placeToolbarAtTop()
        }

        if (getBooleanPreferenceValue(getShowToolbarInlinePreferenceKey(), false)) {
            showToolbarInline()
        }
    }


    // Important: Overwrite onBackPressed and pass it to toolbar.There's no other way that it can get informed of back button presses.
    override fun onBackPressed() {
        if(topInlineToolbar.handlesBackButtonPress() == false && topGroupedCommandsToolbar.handlesBackButtonPress() == false &&
                bottomInlineToolbar.handlesBackButtonPress() == false && bottomGroupedCommandsToolbar.handlesBackButtonPress() == false) {

            super.onBackPressed()
        }
    }

    // only needed if you like to insert images from local device so the user gets asked for permission to access external storage if needed
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsService.onRequestPermissionsResult(requestCode, permissions, grantResults)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        menu.setGroupCheckable(0, true, true)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val mnPlaceToolbarAtTop = menu.findItem(R.id.mnPlaceToolbarAtTop)
        val mnPlaceToolbarAtBottom = menu.findItem(R.id.mnPlaceToolbarAtBottom)

        when(toolbarPlacement) {
            ToolbarPlacement.Top -> mnPlaceToolbarAtTop.isChecked = true
            ToolbarPlacement.Bottom -> mnPlaceToolbarAtBottom.isChecked = true
        }


        val mnToolbarAppearanceInline = menu.findItem(R.id.mnToolbarAppearanceInline)
        val mnToolbarAppearanceGrouped = menu.findItem(R.id.mnToolbarAppearanceGrouped)

        when(toolbarAppearance) {
            ToolbarAppearance.Inline -> mnToolbarAppearanceInline.isChecked = true
            ToolbarAppearance.Grouped -> mnToolbarAppearanceGrouped.isChecked = true
        }


        val mnDarkTheme = menu.findItem(R.id.mnDarkTheme)
        mnDarkTheme.isChecked = getSelectedTheme() == ActivityThemes.Dark

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.mnPlaceToolbarAtTop -> placeToolbarAtTop()
            R.id.mnPlaceToolbarAtBottom -> placeToolbarAtBottom()
            R.id.mnToolbarAppearanceInline -> showToolbarInline()
            R.id.mnToolbarAppearanceGrouped -> showToolbarGrouped()
            // inverted logic here: if MenuItem is not check an gets click then dark theme should get activated (= then MenuItem is checked)
            R.id.mnDarkTheme -> changeTheme(!!! item.isChecked)
            R.id.mnAddHtmlFromWebPage -> addHtmlFromWebPage()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun placeToolbarAtTop() {
        toolbarPlacement = ToolbarPlacement.Top

        savePlaceToolbarAtTopPreference(true)

        setToolbarAppearanceAndPlacement()
    }

    private fun placeToolbarAtBottom() {
        toolbarPlacement = ToolbarPlacement.Bottom

        savePlaceToolbarAtTopPreference(false)

        setToolbarAppearanceAndPlacement()
    }

    private fun savePlaceToolbarAtTopPreference(placeToolbarAtTop: Boolean){
        writeBooleanPreferenceValue(getPlaceToolbarAtTopPreferenceKey(), placeToolbarAtTop)
    }

    private fun getPlaceToolbarAtTopPreferenceKey(): String {
        return getPreferenceKeyWithPackageNamePrefix(PlaceToolbarAtTopPreferenceKeySuffix)
    }

    private fun showToolbarInline() {
        toolbarAppearance = ToolbarAppearance.Inline

        saveShowToolbarInlinePreference(true)

        setToolbarAppearanceAndPlacement()
    }

    private fun showToolbarGrouped() {
        toolbarAppearance = ToolbarAppearance.Grouped

        saveShowToolbarInlinePreference(false)

        setToolbarAppearanceAndPlacement()
    }

    private fun saveShowToolbarInlinePreference(showToolbarInline: Boolean){
        writeBooleanPreferenceValue(getShowToolbarInlinePreferenceKey(), showToolbarInline)
    }

    private fun getShowToolbarInlinePreferenceKey(): String {
        return getPreferenceKeyWithPackageNamePrefix(ShowToolbarInlinePreferenceKeySuffix)
    }

    private fun setToolbarAppearanceAndPlacement() {
        when(toolbarPlacement) {

            ToolbarPlacement.Top -> {
                when(toolbarAppearance) {
                    ToolbarAppearance.Inline -> setToolbar(topInlineToolbar)
                    ToolbarAppearance.Grouped -> setToolbar(topGroupedCommandsToolbar)
                }
            }

            ToolbarPlacement.Bottom -> {
                when(toolbarAppearance) {
                    ToolbarAppearance.Inline -> setToolbar(bottomInlineToolbar)
                    ToolbarAppearance.Grouped -> setToolbar(bottomGroupedCommandsToolbar)
                }
            }
        }
    }

    private fun setToolbar(toolbar: EditorToolbar) {
        editor.editorToolbar?.visibility = View.GONE

        toolbar.visibility = View.VISIBLE

        editor.setEditorToolbarAndOptionsBar(toolbar, fullscreenOptionsBar)
    }


    private fun fullScreenModeChanged(mode: FullscreenWebView.FullscreenMode) {
        val isInFullscreen = mode == FullscreenWebView.FullscreenMode.Enter

        val actionBarContainer = findViewById<View>(R.id.action_bar_container)
        actionBarContainer?.visibility = if(isInFullscreen) View.GONE else View.VISIBLE
    }


    override fun getThemeForName(themeName: String): Theme? {
        if (ActivityThemes.Dark.name == themeName) {
            return ActivityThemes.Dark
        }

        return ActivityThemes.Light
    }

    private fun changeTheme(useDarkTheme: Boolean) {
        setTheme( if (useDarkTheme) ActivityThemes.Dark else ActivityThemes.Light )
    }

    private fun addHtmlFromWebPage() {
        AddHtmlFromWebPageDialog().show(supportFragmentManager) { url: String, _: String ->
            OkHttpWebClient().getAsync(RequestParameters(url)) { response ->
                response.body?.let { html ->
                    parseAndAddWebPageHtml(url, html)
                }

                response.error?.let { error ->
                    // TODO: show error message
                }
            }
        }
    }

    private fun parseAndAddWebPageHtml(url: String, html: String) {
        val readability = Readability4JExtended(url, html)
        val article = readability.parse()

        article.content?.let { readableHtml ->
            editor.javaScriptExecutor.insertHtml(readableHtml)
        }
    }

}
