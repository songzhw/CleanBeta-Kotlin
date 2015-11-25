package cn.six.payx.ui

import android.os.Bundle
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.entity.IPaymentItem
import cn.six.payx.presenter.DepositePresenter
import cn.six.payx.presenter.IPaymentPresenter
import cn.six.payx.presenter.NormalPayPresenter
import kotlinx.android.synthetic.activity_payments.*
import kotlin.properties.Delegates


public class PaymentsActivity : BaseActivity(){
    var adapter : PaymentsAdapter by Delegates.notNull()
    var presenter : IPaymentPresenter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        var type = intent.getStringExtra("showType").toInt()
        when(type){
             0 -> presenter = NormalPayPresenter(this)
             1 -> presenter = DepositePresenter(this)
        }
        adapter = PaymentsAdapter(this)
        presenter.init()

        lvPayments.adapter = adapter;
    }

    fun refreshWholePage(items : MutableList<IPaymentItem> ){
        adapter.data = items
        adapter.notifyDataSetChanged()
    }

}