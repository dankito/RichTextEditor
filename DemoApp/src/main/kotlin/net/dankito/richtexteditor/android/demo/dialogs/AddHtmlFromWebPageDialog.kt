package net.dankito.richtexteditor.android.demo.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import net.dankito.richtexteditor.android.demo.R


class AddHtmlFromWebPageDialog : DialogFragment() {

    companion object {
        val DialogTag = AddHtmlFromWebPageDialog::class.java.name
    }


    private lateinit var edtxtUrl: EditText

    private var urlEnteredListener: ((url: String, title: String) -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_edit_url, container)

        view?.let {
            val btnCancel = view.findViewById<Button>(R.id.btnCancel)
            btnCancel.setOnClickListener { dismiss() }

            val btnOk = view.findViewById<Button>(R.id.btnOk)
            btnOk?.setOnClickListener { enteringUrlDone() }

            val txtUrlLabel = view.findViewById<TextView>(R.id.txtUrlLabel)
            txtUrlLabel.setText(R.string.dialog_add_html_from_web_page_url_label)

            edtxtUrl = view.findViewById(R.id.edtxtUrl)
            edtxtUrl.setHint(R.string.dialog_add_html_from_web_page_url_hint)
            edtxtUrl.setOnEditorActionListener { _, actionId, keyEvent -> handleEditTextUrlAction(actionId, keyEvent) }
            edtxtUrl.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
            }

            val txtTitleLabel = view.findViewById<TextView>(R.id.txtTitleLabel)
            txtTitleLabel.visibility = View.GONE

            val edtxtTitle = view.findViewById<EditText>(R.id.edtxtTitle)
            edtxtTitle.visibility = View.GONE
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) // so that keyboard doesn't cover OK and Cancel buttons
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }


    fun show(fragmentManager: FragmentManager, urlEnteredListener: (url: String, title: String) -> Unit) {
        this.urlEnteredListener = urlEnteredListener

        this.show(fragmentManager, DialogTag)
    }


    private fun handleEditTextUrlAction(actionId: Int, keyEvent: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_DONE || (actionId == EditorInfo.IME_NULL && keyEvent?.action == KeyEvent.ACTION_DOWN)) {
            enteringUrlDone()

            return true
        }

        return false
    }

    private fun enteringUrlDone() {
        val url = edtxtUrl.text.toString().trim() // SwiftKey app enters ' ' at end which causes line break -> trim // TODO: is this reasonable in all cases?

        urlEnteredListener?.invoke(url, "")

        dismiss()
    }

}