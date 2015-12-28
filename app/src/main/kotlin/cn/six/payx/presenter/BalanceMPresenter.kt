package cn.six.payx.presenter

import cn.six.payx.ui.BalanceActivity

public class BalanceMPresenter(var view : BalanceActivity) : IBalancePresenter {
    val model : String by lazy{
        "99.99" // mock the data gathering
    }

    override fun init(){
        view.refreshViewM(model)
    }

}
