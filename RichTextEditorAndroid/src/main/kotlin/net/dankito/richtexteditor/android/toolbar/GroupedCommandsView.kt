package net.dankito.richtexteditor.android.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.extensions.calculateOnMeasure
import net.dankito.richtexteditor.android.extensions.initializeView
import net.dankito.richtexteditor.android.extensions.richTextEditorChanged
import net.dankito.richtexteditor.command.CommandName
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.richtexteditor.command.ToolbarCommandStyle
import net.dankito.utils.android.extensions.ColorExtensions


open class GroupedCommandsView : RelativeLayout, IFloatingView {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }



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



    protected lateinit var contentView: View

    protected val childToolbars = ArrayList<EditorToolbar>()


    protected open fun init() {
        this.visibility = View.GONE

        setBackgroundColor(Color.TRANSPARENT)
    }

    protected open fun addContentView(contentView : View) {
        this.contentView = contentView

        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        params.addRule(RelativeLayout.CENTER_HORIZONTAL)

        addView(contentView, params)
    }

    protected open fun addContentViewAndSetBackgroundToPrimaryColor(contentView : View, addTransparencyToBackground: Boolean = false) {
        addContentView(contentView)

        getPrimaryColor(addTransparencyToBackground)?.let { backgroundColor ->
            contentView.setBackgroundColor(backgroundColor)
        }
    }

    protected open fun getPrimaryColor(addTransparencyToBackground: Boolean): Int? {
        val primaryColorIdentifier = resources.getIdentifier("colorPrimary", "color", (context as Activity).packageName)
        if (primaryColorIdentifier > 0) { // returns 0 in case resource is not found
            var backgroundColor = ContextCompat.getColor(context, primaryColorIdentifier)

            if(addTransparencyToBackground) {
                backgroundColor = ColorExtensions.setTransparency(backgroundColor, ToolbarCommandStyle.GroupedViewsDefaultBackgroundTransparency)
            }

            return backgroundColor
        }

        return null
    }

    protected fun addedChildToolbar(vararg childToolbars: EditorToolbar) {
        this.childToolbars.addAll(childToolbars)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val adjustedHeight = calculateOnMeasure(heightMeasureSpec)

        super.onMeasure(widthMeasureSpec, adjustedHeight)
    }


    open fun initialize(editor: RichTextEditor, command: ToolbarCommand) {
        initializeView(editor, command)

        layoutParams?.let { params ->
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    fun applyStyleToGroupedCommands(style: ToolbarCommandStyle) {
        childToolbars.forEach { toolbar ->
            toolbar.commandStyle = style
            toolbar.styleChanged(true)
        }
    }


    open fun removeCommand(command: CommandName): Boolean {
        return toolbar?.removeCommand(command) == true
    }

}