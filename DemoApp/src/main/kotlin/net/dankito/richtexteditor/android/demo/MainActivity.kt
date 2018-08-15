package net.dankito.richtexteditor.android.demo

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import net.dankito.filechooserdialog.service.PermissionsService
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.AllCommandsEditorToolbar
import net.dankito.richtexteditor.model.DownloadImageConfig
import net.dankito.richtexteditor.model.DownloadImageUiSetting
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var editor: RichTextEditor

    private lateinit var editorToolbar: AllCommandsEditorToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById(R.id.editor) as RichTextEditor

        editorToolbar = findViewById(R.id.editorToolbar) as AllCommandsEditorToolbar
        editorToolbar.editor = editor

        editor.setEditorFontSize(20)
        editor.setPadding((4 * resources.displayMetrics.density).toInt())

        // some properties you also can set on editor
//        editor.setEditorBackgroundColor(Color.YELLOW)
//        editor.setEditorFontColor(Color.MAGENTA)
//        editor.setEditorFontFamily("cursive")

        // show keyboard right at start up
        editor.focusEditorAndShowKeyboardDelayed()

        editor.permissionsService = PermissionsService(this)

        editor.downloadImageConfig = DownloadImageConfig(DownloadImageUiSetting.AllowSelectDownloadFolderInCode,
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "downloaded_images"))
    }


    override fun onBackPressed() {
        if(editorToolbar.handlesBackButtonPress() == false) {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        editor.permissionsService?.onRequestPermissionsResult(requestCode, permissions, grantResults)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
