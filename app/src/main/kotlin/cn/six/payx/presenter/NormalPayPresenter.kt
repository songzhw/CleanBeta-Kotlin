package cn.six.payx.presenter

import cn.six.payx.model.PaymentsModel
import cn.six.payx.ui.PaymentsActivity
import kotlin.properties.Delegates

public class NormalPayPresenter(val actv : PaymentsActivity) : IPaymentPresenter {
    override var model : PaymentsModel by Delegates.notNull()
    override var view : PaymentsActivity by Delegates.notNull()

    override fun init() {
        view = actv
        model = PaymentsModel()

        // omit the process of getting data from server or db in Presenter
        model.initData()
        view.refreshWholePage(model.payments)
    }



}