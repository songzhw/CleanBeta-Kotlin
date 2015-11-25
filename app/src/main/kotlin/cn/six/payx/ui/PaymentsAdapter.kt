package cn.six.payx.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cn.six.payx.R
import cn.six.payx.entity.IPaymentItem
import kotlin.properties.Delegates

public class PaymentsAdapter(var ctx : Context) : BaseAdapter() {
    var data : MutableList<IPaymentItem> by Delegates.notNull()

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any? {
        return data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var holder : ViewHolder? = null
        var view : View? = null
        if(convertView == null){
            holder = ViewHolder()
            view = LayoutInflater.from(ctx).inflate(R.layout.item_payment, null)
            holder.tvPayment = view?.findViewById(R.id.tvPaymentItem) as TextView
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val item = data.get(position)
        val text = item.bank + "  " + item.type
        holder.tvPayment.text = text

        return view
    }

    private class ViewHolder {
        var tvPayment: TextView  by Delegates.notNull()
    }
}