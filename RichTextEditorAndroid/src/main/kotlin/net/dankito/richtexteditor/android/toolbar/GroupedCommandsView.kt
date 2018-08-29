package net.dankito.richtexteditor.android.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.extensions.calculateOnMeasure
import net.dankito.richtexteditor.android.extensions.initializeView
import net.dankito.richtexteditor.android.extensions.updatePosition
import net.dankito.richtexteditor.command.ToolbarCommand


open class GroupedCommandsView : RelativeLayout, IFloatingView {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }



    override var command: ToolbarCommand? = null

    override var editor: RichTextEditor? = null

    override var toolbar: EditorToolbar? = null

    override var lastEditorHeight = 0

    override var setMaxHeightOnNextMeasurement = false

    override var hasEditorHeightChanged = true

    var addTransparencyToBackground: Boolean = true


    protected lateinit var contentView: View


    protected open fun init() {
        this.visibility = View.GONE

        setInitialBackgroundColor()

        layoutParams?.let { params ->
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    protected open fun setInitialBackgroundColor() {
        this.setBackgroundColor(Color.WHITE)

        val primaryColorIdentifier = resources.getIdentifier("colorPrimary", "color", (context as Activity).packageName)
        if (primaryColorIdentifier > 0) { // returns 0 in case resource is not found
            val primaryColor = ContextCompat.getColor(context, primaryColorIdentifier)

            val backgroundColor =
                    if (addTransparencyToBackground)
                        Color.argb(200, Color.red(primaryColor), Color.green(primaryColor), Color.blue(primaryColor))
                    else
                        primaryColor

            setBackgroundColor(backgroundColor)
        }
    }

    protected open fun addContentView(contentView : View) {
        this.contentView = contentView

        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        params.addRule(ALIGN_PARENT_LEFT)
        params.addRule(ALIGN_PARENT_RIGHT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.addRule(ALIGN_PARENT_START)
            params.addRule(ALIGN_PARENT_END)
        }

        addView(contentView, params)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val adjustedHeightAndUpdatePosition = calculateOnMeasure(widthMeasureSpec, heightMeasureSpec)
        val adjustedHeight = adjustedHeightAndUpdatePosition.first
        val updatePosition = adjustedHeightAndUpdatePosition.second

        super.onMeasure(widthMeasureSpec, adjustedHeight)

        if(updatePosition) {
            updatePosition()
        }
    }


    open fun initialize(editor: RichTextEditor, command: ToolbarCommand) {
        initializeView(editor, command)
    }

}