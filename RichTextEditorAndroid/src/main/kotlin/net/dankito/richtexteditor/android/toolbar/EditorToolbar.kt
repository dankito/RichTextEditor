package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.Command


open class EditorToolbar : HorizontalScrollView {


    constructor(context: Context) : super(context) { initToolbar(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initToolbar(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initToolbar(context) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initToolbar(context) }


    var editor: RichTextEditor? = null
        set(value) {
            field = value

            setRichTextEditorOnCommands(value)
        }


    private lateinit var linearLayout: LinearLayout

    private val commands = HashMap<Command, View>()


    private fun initToolbar(context: Context) {
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL

        addView(linearLayout)
    }


    fun addCommand(command: Command) {
        val commandView = ImageButton(context)
        commandView.setOnClickListener { command.commandInvoked() }

        linearLayout.addView(commandView)

        commands.put(command, commandView)

        command.editor = editor
        command.commandView = commandView

        applyCommandStyle(command, commandView)
    }

    private fun applyCommandStyle(command: Command, commandView: ImageButton) {
        commandView.setImageResource(command.iconResourceId)
        commandView.scaleType = ImageView.ScaleType.FIT_CENTER

        commandView.setBackgroundColor(command.style.backgroundColor)

        val displayDensity = context.resources.displayMetrics.density

        val padding = getPixelSizeForDisplay(command.style.paddingDp, displayDensity)
        commandView.setPadding(padding, padding, padding, padding)

        val layoutParams = commandView.layoutParams as LinearLayout.LayoutParams

        layoutParams.width = getLayoutSize(command.style.widthDp, displayDensity)
        layoutParams.height = getLayoutSize(command.style.heightDp, displayDensity)

        val rightMargin = getPixelSizeForDisplay(command.style.marginRightDp, displayDensity)
        layoutParams.rightMargin = rightMargin
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.marginEnd = rightMargin
        }
    }

    private fun getPixelSizeForDisplay(deviceIndependentPixel: Int, displayDensity: Float): Int {
        return (deviceIndependentPixel * displayDensity).toInt()
    }

    private fun getLayoutSize(sizeInDp: Int, displayDensity: Float): Int {
        if(sizeInDp >= 0) {
            return getPixelSizeForDisplay(sizeInDp, displayDensity)
        }
        else { // e.g. ViewGroup.LayoutParams.MATCH_PARENT
            return sizeInDp
        }
    }


    private fun setRichTextEditorOnCommands(editor: RichTextEditor?) {
        commands.keys.forEach {
            it.editor = editor
        }
    }


}