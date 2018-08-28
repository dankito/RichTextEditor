package net.dankito.richtexteditor.android.demo

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.dankito.richtexteditor.android.FullscreenWebView
import net.dankito.utils.permissions.PermissionsService
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.AllCommandsEditorToolbar
import net.dankito.richtexteditor.android.toolbar.GroupedCommandsEditorToolbar
import net.dankito.richtexteditor.model.DownloadImageConfig
import net.dankito.richtexteditor.model.DownloadImageUiSetting
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var editor: RichTextEditor

    private lateinit var editorToolbar: AllCommandsEditorToolbar

    private lateinit var bottomGroupedCommandsToolbar: GroupedCommandsEditorToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById(R.id.editor) as RichTextEditor

        editorToolbar = findViewById(R.id.editorToolbar) as AllCommandsEditorToolbar
        editorToolbar.editor = editor

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

        editor.permissionsService = PermissionsService(this)

        editor.downloadImageConfig = DownloadImageConfig(DownloadImageUiSetting.AllowSelectDownloadFolderInCode,
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "downloaded_images"))

        // semi transparent options bar shown when entering fullscreen in viewing mode
        fullscreenOptionsBar.showMarkSelectedTextButton(editor)
        editor.setEditorToolbarAndOptionsBar(bottomGroupedCommandsToolbar, fullscreenOptionsBar)
        editor.changeFullscreenModeListener = { mode -> fullScreenModeChanged(mode) }

        editor.enterEditingMode()
        // show editor in viewing, not editing, mode. Tap on editor switches back to editing mode then.
        // But to be able to enter fullscreen mode you need to set HTML that fills at least on display page.
//        editor.enterViewingMode()
    }


    override fun onBackPressed() {
        if(editorToolbar.handlesBackButtonPress() == false &&
                bottomGroupedCommandsToolbar.handlesBackButtonPress() == false) {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        editor.permissionsService?.onRequestPermissionsResult(requestCode, permissions, grantResults)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun fullScreenModeChanged(mode: FullscreenWebView.FullscreenMode) {
        val isInFullscreen = mode == FullscreenWebView.FullscreenMode.Enter

        val actionBarContainer = findViewById(R.id.action_bar_container)
        actionBarContainer.visibility = if(isInFullscreen) View.GONE else View.VISIBLE
    }

}
