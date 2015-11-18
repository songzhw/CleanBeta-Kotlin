package cn.six.payx.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cn.six.payx.R
import kotlin.properties.Delegates

public class CardAdapter(var ctx : Context) : BaseAdapter(){
    var data : List<String> by Delegates.notNull()

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any? {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var holder : ViewHolder? = null
        var view : View? = null
        if(convertView == null){
            holder = ViewHolder()
            view = LayoutInflater.from(ctx).inflate(R.layout.item_card, null)
            holder.tvBankName = view?.findViewById(R.id.tvBankName) as TextView
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        holder.tvBankName.text = data.get(position)

        return view
    }

    private class ViewHolder {
        var tvBankName: TextView  by Delegates.notNull()
    }
}

