package net.dankito.richtexteditor.android.util

import android.text.Editable
import android.text.TextWatcher


class GenericTextWatcher(private val onTextChanged: ((text: CharSequence, start: Int, before: Int, count: Int) -> Unit)?)
    : TextWatcher {


    override fun afterTextChanged(editable: Editable?) {

    }

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(text, start, before, count)
    }

}