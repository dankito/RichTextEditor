package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
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
        val button = ImageButton(context)
        button.setOnClickListener { command.commandInvoked() }

        button.setImageResource(command.iconResourceId)
        button.scaleType = ImageView.ScaleType.FIT_CENTER

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.setBackgroundColor(context.getColor(command.backgroundColorResourceId))
        }
        else {
            button.setBackgroundColor(context.resources.getColor(command.backgroundColorResourceId))
        }

        val displayDensity = context.resources.displayMetrics.density

        val padding = getPixelSizeForDisplay(command.paddingDp, displayDensity)
        button.setPadding(padding, padding, padding, padding)

        linearLayout.addView(button)

        commands.put(command, button)
        command.editor = editor

        val layoutParams = button.layoutParams as LinearLayout.LayoutParams

        layoutParams.width = getPixelSizeForDisplay(command.widthDp, displayDensity)
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        val rightMargin = getPixelSizeForDisplay(command.marginRightDp, displayDensity)
        layoutParams.rightMargin = rightMargin
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.marginEnd = rightMargin
        }
    }

    private fun getPixelSizeForDisplay(deviceIndependentPixel: Int, displayDensity: Float): Int {
        return (deviceIndependentPixel * displayDensity).toInt()
    }


    private fun setRichTextEditorOnCommands(editor: RichTextEditor?) {
        commands.keys.forEach {
            it.editor = editor
        }
    }


}