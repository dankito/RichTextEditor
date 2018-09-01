package net.dankito.richtexteditor.android.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import net.dankito.richtexteditor.android.AndroidCommandView
import net.dankito.richtexteditor.android.RichTextEditor
import net.dankito.richtexteditor.android.toolbar.EditorToolbar
import net.dankito.richtexteditor.android.toolbar.IFloatingView
import net.dankito.richtexteditor.command.ToolbarCommand
import net.dankito.utils.android.animation.ShowHideViewAnimator
import net.dankito.utils.android.extensions.executeActionAfterMeasuringSize
import net.dankito.utils.android.extensions.getLocationOnScreenY


private val animator = ShowHideViewAnimator()


fun IFloatingView.initializeView(editor: RichTextEditor, command: ToolbarCommand) {
    this.editor = editor
    this.command = command

    editor.viewTreeObserver.addOnGlobalLayoutListener {
        hasEditorHeightChanged = editor.measuredHeight != lastEditorHeight // most probably due to keyboard show/hide
    }

    findToolbar(command.commandView as? AndroidCommandView)?.let { toolbar ->
        this.toolbar = toolbar
        toolbar.addCommandInvokedListener { commandInvoked(it) }

        addToLayout(editor, toolbar)
    }
}

private fun IFloatingView.commandInvoked(command: ToolbarCommand) {
    if(isVisible() && command != this.command) { // another command has been invoked but the SelectValueView is visible -> hide
        hideView()
    }
}

fun IFloatingView.isVisible(): Boolean {
    (this as? View)?.let { view ->
        return view.visibility == View.VISIBLE
    }

    return false
}


fun IFloatingView.updatePosition() {
    (this as? View)?.let { view ->
        editor?.let { editor ->
            toolbar?.let { toolbar ->
                view.y =
                        if(isToolbarBelowEditor(editor, toolbar)) {
                            getToolbarTop(toolbar) - measuredHeight
                        }
                        else {
                            toolbar.bottom.toFloat()
                        }
            }
        }
    }
}


fun IFloatingView.findToolbar(commandView: AndroidCommandView?): EditorToolbar? {
    var parent = commandView?.view?.parent

    while(parent != null) {
        if(parent is EditorToolbar) {
            return parent
        }

        parent = parent.parent
    }

    return null
}

fun IFloatingView.addToLayout() {
    editor?.let { editor ->
        toolbar?.let { toolbar ->
            addToLayout(editor, toolbar)
        }
    }
}

fun IFloatingView.addToLayout(editor: RichTextEditor, toolbar: EditorToolbar) {
    (this as? View)?.let { view ->
        findParentViewGroup(editor)?.let { toolbarParent ->
            toolbarParent.addView(view)

            (view.layoutParams as? RelativeLayout.LayoutParams)?.let { layoutParams ->
                if(isToolbarBelowEditor(editor, toolbar)) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                }
                else {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                }
            }
        }
    }
}

private fun findParentViewGroup(view: View): ViewGroup? {
    var parent = view.parent

    while(parent != null) {
        if(parent is ViewGroup) {
            return parent
        }

        parent = parent.parent
    }

    return null
}


fun IFloatingView.calculateOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) : Pair<Int, Boolean> {
    var adjustedHeight = heightMeasureSpec
    var updatePosition = false

    editor?.let { editor ->
        if(lastEditorHeight != editor.measuredHeight) {
            lastEditorHeight = editor.measuredHeight
            adjustedHeight = View.MeasureSpec.makeMeasureSpec(editor.measuredHeight, View.MeasureSpec.AT_MOST)
            updatePosition = true
        }
        else if(setMaxHeightOnNextMeasurement) {
            setMaxHeightOnNextMeasurement = false
            adjustedHeight = View.MeasureSpec.makeMeasureSpec(editor.measuredHeight, View.MeasureSpec.AT_MOST)
        }
    }

    return Pair(adjustedHeight, updatePosition)
}


fun IFloatingView.showView() {
    (this as? View)?.let { view ->
        view.visibility = android.view.View.VISIBLE
        setMaxHeightOnNextMeasurement = true
        editor?.bringChildToFront(view)

        animateShowView()
    }
}

fun IFloatingView.hideView() {
    (this as? View)?.let { view ->
        editor?.let { editor ->
            toolbar?.let { toolbar ->
                val isToolbarBelowEditor = isToolbarBelowEditor(editor, toolbar)
                val startPosition = view.top
                val endPosition = if(isToolbarBelowEditor) view.bottom else -1 * view.measuredHeight

                playAnimation(view, false, startPosition.toFloat(), endPosition.toFloat())
            }
        }
    }
}


fun IFloatingView.animateShowView() {
    (this as? View)?.let { view ->
        view.executeActionAfterMeasuringSize(hasEditorHeightChanged) {
            animateShowViewAfterMeasuringHeight(view)
        }

        hasEditorHeightChanged = false
    }
}

private fun IFloatingView.animateShowViewAfterMeasuringHeight(view: View) {
    editor?.let { editor ->
        toolbar?.let { toolbar ->
            val isToolbarBelowEditor = isToolbarBelowEditor(editor, toolbar)

            if(isToolbarBelowEditor) {
                val startPosition = getToolbarTop(toolbar)
                val endPosition = startPosition - view.measuredHeight

                playAnimation(view, true, startPosition, endPosition)
            }
            else {
                val endPosition = 0f
                val startPosition = endPosition - view.measuredHeight

                playAnimation(view, true, startPosition, endPosition)
            }
        }
    }
}

private fun getToolbarTop(toolbar: EditorToolbar): Float {
    var toolbarTop = toolbar.top.toFloat()
    var parent = toolbar.parent

    while(toolbarTop == 0f && parent != null) { // then toolbar is most likely embedded into another view -> get parent view's top
        (toolbar.parent as? ViewGroup)?.top?.let { parentTop ->
            toolbarTop = parentTop.toFloat()
            parent = parent?.parent
        }
    }

    return toolbarTop
}

private fun playAnimation(view: View, show: Boolean, yStart: Float, yEnd: Float, animationEndListener: (() -> Unit)? = null) {
    animator.playVerticalAnimation(view, show, yStart, yEnd, animationEndListener = animationEndListener)
}


fun IFloatingView.isToolbarBelowEditor(): Boolean {
    editor?.let { editor ->
        toolbar?.let { toolbar ->
            return isToolbarBelowEditor(editor, toolbar)
        }
    }

    return false
}

fun IFloatingView.isToolbarBelowEditor(editor: RichTextEditor, toolbar: EditorToolbar): Boolean {
    val editorY = editor.getLocationOnScreenY()
    val toolbarY = toolbar.getLocationOnScreenY()

    return toolbarY > editorY
}