package net.dankito.richtexteditor.java.fx.command

import net.dankito.richtexteditor.Icon
import net.dankito.richtexteditor.command.InsertLinkCommandBase
import net.dankito.richtexteditor.java.fx.JavaFXIcon


class InsertLinkCommand(icon: Icon = JavaFXIcon.fromResourceName("ic_insert_link_black_36dp.png")) : InsertLinkCommandBase(icon) {

//    override var editor: RichTextEditor? = null


    override fun selectLinkToInsert(linkSelected: (url: String, title: String) -> Unit) {
//        (editor?.context as? FragmentActivity)?.let { activity ->
//            val dialog = EditUrlDialog()
//
//            dialog.show(activity.supportFragmentManager) { url, title ->
//                linkSelected(url, title)
//            }
//        }
    }

}