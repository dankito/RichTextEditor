package net.dankito.richtexteditor.android.command.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.dialog_edit_url.*
import kotlinx.android.synthetic.main.dialog_edit_url.view.*
import net.dankito.richtexteditor.android.R


open class EditUrlDialog : DialogFragment() {

    companion object {
        val DialogTag = EditUrlDialog::class.java.name
    }


    protected var urlEnteredListener: ((url: String, title: String) -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_edit_url, container)

        view?.let {
            view.btnCancel.setOnClickListener { dismiss() }

            view.btnOk?.setOnClickListener { enteringUrlDone() }

            view.edtxtUrl.setText(R.string.dialog_edit_url_default_value)
            view.edtxtUrl.selectAll()

            view.edtxtUrl.setOnEditorActionListener { _, actionId, keyEvent -> handleEditTextUrlAction(actionId, keyEvent) }
            view.edtxtUrl.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
            }
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) // so that keyboard doesn't cover OK and Cancel buttons
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }


    open fun show(fragmentManager: FragmentManager, urlEnteredListener: (url: String, title: String) -> Unit) {
        this.urlEnteredListener = urlEnteredListener

        this.show(fragmentManager, DialogTag)
    }


    protected open fun handleEditTextUrlAction(actionId: Int, keyEvent: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_DONE || (actionId == EditorInfo.IME_NULL && keyEvent?.action == KeyEvent.ACTION_DOWN)) {
            enteringUrlDone()

            return true
        }

        return false
    }

    protected open fun enteringUrlDone() {
        val url = edtxtUrl.text.toString().trim() // SwiftKey app enters ' ' at end which causes line break -> trim // TODO: is this reasonable in all cases?

        var title = edtxtTitle.text.toString().trim()
        if(title.isEmpty()) {
            title = url
        }

        urlEnteredListener?.invoke(url, title)

        dismiss()
    }

}