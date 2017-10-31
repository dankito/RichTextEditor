package net.dankito.richtexteditor.android.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.DefaultEditorToolbar


class MainActivity : AppCompatActivity() {

    private lateinit var editor: RichTextEditor

    private lateinit var editorToolbar: DefaultEditorToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById<RichTextEditor>(R.id.editor)

        editorToolbar = findViewById<DefaultEditorToolbar>(R.id.editorToolbar)
        editorToolbar.editor = editor
    }

}
