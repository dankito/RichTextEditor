package net.dankito.richtexteditor.android.toolbar

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ListView
import android.widget.RelativeLayout
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.command.ToolbarCommand
import net.dankito.richtexteditor.android.command.SelectValueCommand




class SelectValueView: ListView {

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { init(context, attrs) }


    companion object {
        private const val AnimationDurationMillis = 250L
    }


    var values: List<CharSequence> = ArrayList()
        set(value) {
            field = value

            valuesAdapter.items = value
        }

    var itemSelectedListener: ((Int) -> Unit)? = null


    private var command: SelectValueCommand? = null

    private var editor: RichTextEditor? = null

    private var toolbar: EditorToolbar? = null

    private val valuesAdapter = SelectValueAdapter()

    private var lastEditorHeight = 0

    private var setMaxHeightOnNextMeasurement = false


    private fun init(context: Context, attrs: AttributeSet?) {
        this.visibility = View.GONE
        this.setBackgroundColor(Color.WHITE)

        this.adapter = valuesAdapter

        setOnItemClickListener { _, _, position, _ -> itemSelected(position) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var adjustedHeight = heightMeasureSpec
        var updatePosition = false

        editor?.let { editor ->
            if(lastEditorHeight != editor.measuredHeight) {
                lastEditorHeight = editor.measuredHeight
                adjustedHeight = MeasureSpec.makeMeasureSpec(editor.measuredHeight, MeasureSpec.AT_MOST)
                updatePosition = true
            }
            else if(setMaxHeightOnNextMeasurement) {
                setMaxHeightOnNextMeasurement = false
                adjustedHeight = MeasureSpec.makeMeasureSpec(editor.measuredHeight, MeasureSpec.AT_MOST)
            }
        }

        super.onMeasure(widthMeasureSpec, adjustedHeight)

        if(updatePosition) {
            updatePosition()
        }
    }

    private fun updatePosition() {
        editor?.let { editor ->
            toolbar?.let { toolbar ->
                this.y =
                    if(isToolbarBelowEditor(editor, toolbar)) {
                        toolbar.top.toFloat() - measuredHeight
                    }
                    else {
                        toolbar.bottom.toFloat()
                    }
            }
        }
    }


    fun initialize(editor: RichTextEditor, selectValueCommand: SelectValueCommand, valuesDisplayTexts: List<CharSequence>, itemSelectedListener: ((Int) -> Unit)? = null) {
        this.command = selectValueCommand
        this.editor = editor
        this.values = valuesDisplayTexts
        this.itemSelectedListener = itemSelectedListener

        var parent = selectValueCommand.commandView?.parent

        while(parent != null) {
            if(parent is EditorToolbar) {
                this.toolbar = parent
                parent.addCommandInvokedListener { commandInvoked(it) }

                addToLayout(editor, parent)

                break
            }

            parent = parent.parent
        }
    }

    private fun addToLayout(editor: RichTextEditor, toolbar: EditorToolbar) {
        (editor as? ViewGroup)?.let { toolbarParent ->
            toolbarParent.addView(this)

            (this.layoutParams as? RelativeLayout.LayoutParams)?.let { layoutParams ->
                if(isToolbarBelowEditor(editor, toolbar)) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                }
                else {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                }
            }
        }
    }

    fun toggleShowView() {
        if(isVisible()) {
            hideView()
        }
        else {
            showView()
        }
    }

    private fun commandInvoked(command: ToolbarCommand) {
        if(isVisible() && command != this.command) {
            hideView()
        }
    }

    private fun isVisible(): Boolean {
        return this.visibility == View.VISIBLE
    }


    private fun itemSelected(position: Int) {
        hideView()

        itemSelectedListener?.invoke(position)
    }


    private fun showView() {
        this.visibility = View.VISIBLE
        setMaxHeightOnNextMeasurement = true
        editor?.bringChildToFront(this)

        animateShowView()
    }

    private fun hideView() {
        editor?.let { editor ->
            toolbar?.let { toolbar ->
                val isToolbarBelowEditor = isToolbarBelowEditor(editor, toolbar)
                val startPosition = if(isToolbarBelowEditor) this.top else this.bottom
                val endPosition = if(isToolbarBelowEditor) this.bottom else this.top

                playAnimation(false, startPosition.toFloat(), endPosition.toFloat()) {
                    this.visibility = View.GONE
                }
            }
        }
    }


    private fun animateShowView() {
        if(this.measuredHeight == 0) { // in this case we have to wait till height is determined -> set OnGlobalLayoutListener
            var layoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null // have to do it that complicated otherwise in OnGlobalLayoutListener we cannot access layoutListener variable
            layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                removeOnGlobalLayoutListener(layoutListener)

                animateShowViewAfterMeasuringHeight()
            }

            this.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        }
        else {
            animateShowViewAfterMeasuringHeight()
        }
    }

    private fun animateShowViewAfterMeasuringHeight() {
        editor?.let { editor ->
            toolbar?.let { toolbar ->
                val isToolbarBelowEditor = isToolbarBelowEditor(editor, toolbar)
                val startPosition = if(isToolbarBelowEditor) toolbar.top.toFloat() else toolbar.bottom.toFloat()
                val endPosition = if(isToolbarBelowEditor) startPosition - this.measuredHeight else startPosition + this.measuredHeight

                playAnimation(true, startPosition, endPosition)
            }
        }
    }

    private fun isToolbarBelowEditor(editor: RichTextEditor, toolbar: EditorToolbar) =
            toolbar.y > editor.y

    private fun playAnimation(show: Boolean, yStart: Float, yEnd: Float, animationEndListener: (() -> Unit)? = null) {
        val yAnimator = ObjectAnimator
                .ofFloat(this, View.Y, yStart, yEnd)
                .setDuration(AnimationDurationMillis)
        yAnimator.interpolator = if(show) AccelerateInterpolator() else DecelerateInterpolator()

        val alphaAnimator = ObjectAnimator
                .ofFloat(this, View.ALPHA, if(show) 0f else 1f, if(show) 1f else 0f)
                .setDuration(AnimationDurationMillis)
        alphaAnimator.interpolator = if(show) AccelerateInterpolator() else DecelerateInterpolator()

        val animatorSet = AnimatorSet()

        yAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationRepeat(animation: Animator) { }

            override fun onAnimationCancel(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                animationEndListener?.invoke()
            }

        })

        animatorSet.playTogether(yAnimator, alphaAnimator)
        animatorSet.start()
    }

    private fun removeOnGlobalLayoutListener(layoutListener: ViewTreeObserver.OnGlobalLayoutListener?) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            this.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
        }
        else {
            this.viewTreeObserver.removeGlobalOnLayoutListener(layoutListener)
        }
    }

}