package cn.six.payx.presenter

import cn.six.payx.entity.IPaymentItem
import cn.six.payx.model.PaymentsModel
import cn.six.payx.ui.PaymentsActivity
import kotlin.properties.Delegates

// deposite from one of my bank debit cards to account balance
// note: credit card can not be used to deposit. It's maybe money laundering.
public class DepositePresenter (val actv : PaymentsActivity) : IPaymentPresenter {
    override var model : PaymentsModel by Delegates.notNull()
    override var view : PaymentsActivity by Delegates.notNull()

    override fun init() {
        view = actv
        model = PaymentsModel()

        // omit the process of getting data from server or db in Presenter
        model.initData()
        var debits : MutableList<IPaymentItem> = arrayListOf()
        model.payments.forEach { item ->
            if(item.type == "Debit Card"){
                debits.add(item)
            }
        }
        view.refreshWholePage(debits)
    }

}
