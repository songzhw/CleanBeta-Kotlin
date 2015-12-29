package cn.six.payx.presenter

import cn.six.payx.ui.BalanceActivity

public class BalanceLPresenter(var view : BalanceActivity) : IBalancePresenter {

    override fun init(){
        view.refreshViewL(model)
    }

}