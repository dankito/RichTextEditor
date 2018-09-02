package net.dankito.richtexteditor.android.util

import android.view.MotionEvent


interface IHandlesTouch {

    fun handlesTouch(event: MotionEvent): Boolean

}