package net.dankito.richtexteditor.android.toolbar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_item_select_value.view.*
import net.dankito.richtexteditor.android.R


class SelectValueAdapter : BaseAdapter() {

    var items: List<CharSequence> = listOf()
        set(value) {
            field = value

            notifyDataSetChanged()
        }


    override fun getCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: (parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.list_item_select_value, parent, false)

        view.txtItemValue.text = items[position]

        return view
    }

}