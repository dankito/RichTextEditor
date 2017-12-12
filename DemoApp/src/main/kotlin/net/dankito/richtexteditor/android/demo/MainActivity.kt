package net.dankito.richtexteditor.android.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.AllCommandsEditorToolbar


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

        // show keyboard right at start up
        editor.focusEditorAndShowKeyboardDelayed()
    }

}
