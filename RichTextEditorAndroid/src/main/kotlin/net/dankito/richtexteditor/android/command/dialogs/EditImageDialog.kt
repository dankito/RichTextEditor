package net.dankito.richtexteditor.android.command.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.dialog_edit_image.*
import kotlinx.android.synthetic.main.dialog_edit_image.view.*
import net.dankito.richtexteditor.android.R

class EditImageDialog : DialogFragment() {

    companion object {
        val DialogTag = EditImageDialog::class.java.name
    }


    private var imageUrlEnteredListener: ((imageUrl: String, alternateText: String) -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_edit_image, container)

        view?.let {
            view.btnCancel.setOnClickListener { dismiss() }

            view.btnOk?.setOnClickListener { enteringImageUrlDone() }

            view.edtxtImageUrl.setOnEditorActionListener { _, actionId, keyEvent -> handleEditTextUrlAction(actionId, keyEvent) }
            view.edtxtImageUrl.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
            }
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) // so that keyboard doesn't cover OK and Cancel buttons
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }


    fun show(fragmentManager: FragmentManager, imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit) {
        this.imageUrlEnteredListener = imageUrlEnteredListener

        this.show(fragmentManager, DialogTag)
    }


    private fun handleEditTextUrlAction(actionId: Int, keyEvent: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_DONE || (actionId == EditorInfo.IME_NULL && keyEvent?.action == KeyEvent.ACTION_DOWN)) {
            enteringImageUrlDone()

            return true
        }

        return false
    }

    private fun enteringImageUrlDone() {
        val imageUrl = edtxtImageUrl.text.toString().trim() // SwiftKey app enters ' ' at end which causes line break -> trim // TODO: is this reasonable in all cases?

        var alternateText = edtxtAlternateText.text.toString().trim()
        if(alternateText.isBlank()) {
            alternateText = imageUrl
        }

        imageUrlEnteredListener?.invoke(imageUrl, alternateText)

        dismiss()
    }

}