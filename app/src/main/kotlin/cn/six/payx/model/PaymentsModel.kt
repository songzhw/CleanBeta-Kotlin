package cn.six.payx.model

import cn.six.payx.entity.AccountBalance
import cn.six.payx.entity.CreditCard
import cn.six.payx.entity.DebitCard
import cn.six.payx.entity.IPaymentItem
import kotlin.properties.Delegates

public class PaymentsModel {
    var payments : MutableList<IPaymentItem> by Delegates.notNull()

    fun initData(){
        // omit the process of getting data from server or db in Presenter

        var balance = AccountBalance()
        var citiCredit = CreditCard("citi")
        var citiDebit = DebitCard("citi")
        var royalCredit = CreditCard("royal")
        payments = arrayListOf(balance, royalCredit, citiCredit, citiDebit)
    }
}