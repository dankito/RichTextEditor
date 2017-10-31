package net.dankito.richtexteditor.android.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.dankito.richtexteditor.android.RichTextEditor


class MainActivity : AppCompatActivity() {

    private lateinit var editor: RichTextEditor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById<RichTextEditor>(R.id.editor)
    }

}
