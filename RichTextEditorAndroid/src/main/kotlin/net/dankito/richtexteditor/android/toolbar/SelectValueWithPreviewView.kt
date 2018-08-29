package net.dankito.richtexteditor.android.toolbar

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import net.dankito.richtexteditor.Color
import net.dankito.richtexteditor.android.R
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.SelectValueWithPreviewCommand
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.utils.android.extensions.getDimension
import net.dankito.utils.android.extensions.setRightMargin


open class SelectValueWithPreviewView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    protected var editor: RichTextEditor? = null

    protected var command: ToolbarCommand? = null


    val icon = ImageView(context)

    protected val preview = TextView(context)

    protected var values: List<CharSequence> = ArrayList()

    protected var itemSelectedListener: ((Int) -> Unit)? = null


    init {
        initView()
    }

    protected open fun initView() {
        this.orientation = HORIZONTAL

        val iconParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        iconParams.gravity = Gravity.START
        addView(icon, iconParams)

        val previewParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        previewParams.setRightMargin(context.getDimension(R.dimen.select_value_with_preview_view_margin_right))
        previewParams.gravity = Gravity.CENTER_VERTICAL

        preview.gravity = Gravity.CENTER_VERTICAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            preview.textAlignment = TextView.TEXT_ALIGNMENT_GRAVITY
        }
        addView(preview, previewParams)
    }


    fun initialize(editor: RichTextEditor, command: SelectValueWithPreviewCommand, valuesDisplayTexts: List<CharSequence>,
                   itemSelectedListener: ((Int) -> Unit)? = null) {
        this.editor = editor
        this.command = command
        this.values = valuesDisplayTexts
        this.itemSelectedListener = itemSelectedListener

        if(preview.text.isBlank()) {
            setPreviewText(command.getDefaultPreview())
        }
    }


    fun selectValue() {
        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Select a value") // TODO

        builder.setItems(values.toTypedArray()) { _, which ->
            itemSelectedListener?.invoke(which)
        }

        builder.show()
    }


    fun setPreviewText(previewText: CharSequence) {
        preview.text = previewText
    }

    fun setTintColor(color: Color) {
        icon.setColorFilter(color.toInt())

        preview.setTextColor(color.toInt())
    }
}