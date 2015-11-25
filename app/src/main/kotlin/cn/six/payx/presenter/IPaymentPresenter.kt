package cn.six.payx.presenter

import cn.six.payx.model.PaymentsModel
import cn.six.payx.ui.PaymentsActivity

public interface IPaymentPresenter{
    var model: PaymentsModel
    val view : PaymentsActivity

    fun init()
}