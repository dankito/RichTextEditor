package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.SelectValueCommand
import net.dankito.richtexteditor.android.extensions.calculateOnMeasure
import net.dankito.richtexteditor.android.extensions.hideView
import net.dankito.richtexteditor.android.extensions.initializeView
import net.dankito.richtexteditor.android.extensions.richTextEditorChanged
import net.dankito.richtexteditor.command.ToolbarCommand


class SelectValueView: ListView, IFloatingView {

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }


    var values: List<CharSequence> = ArrayList()
        set(value) {
            field = value

            valuesAdapter.items = value
        }

    var itemSelectedListener: ((Int) -> Unit)? = null


    override var command: ToolbarCommand? = null

    override var editor: RichTextEditor? = null
        set(value) {
            field = value
            richTextEditorChanged(value)
        }

    override var toolbar: EditorToolbar? = null

    override var lastEditorHeight = 0

    override var setMaxHeightOnNextMeasurement = false

    override var hasEditorHeightChanged = true

    private val valuesAdapter = SelectValueAdapter()


    private fun init() {
        this.visibility = View.GONE

        this.adapter = valuesAdapter

        setOnItemClickListener { _, _, position, _ -> itemSelected(position) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val adjustedHeight = calculateOnMeasure(heightMeasureSpec)

        super.onMeasure(widthMeasureSpec, adjustedHeight)
    }


    fun initialize(editor: RichTextEditor, selectValueCommand: SelectValueCommand, valuesDisplayTexts: List<CharSequence>, itemSelectedListener: ((Int) -> Unit)? = null) {
        this.values = valuesDisplayTexts
        this.itemSelectedListener = itemSelectedListener

        initializeView(editor, selectValueCommand)
    }


    private fun itemSelected(position: Int) {
        hideView()

        itemSelectedListener?.invoke(position)
    }

}