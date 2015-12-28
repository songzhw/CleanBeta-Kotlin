package cn.six.payx.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.presenter.BalanceLPresenter
import cn.six.payx.presenter.BalanceMPresenter
import cn.six.payx.presenter.IBalancePresenter
import cn.six.payx.util.authen
import cn.six.payx.util.fingerMgr
import cn.six.payx.util.onPermitted
import kotlinx.android.synthetic.activity_balance.*
import kotlin.properties.Delegates

public class BalanceActivity : BaseActivity(){
    var presenter : IBalancePresenter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        if(Build.VERSION.SDK_INT < 23){
            presenter = BalanceLPresenter(this)
        } else {
            presenter = BalanceMPresenter(this)
        }
        presenter.init()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        onPermitted(requestCode, permissions, grantResults)
    }

    fun refreshViewM(balance : String){
        tvBalanceInfo.setText("Your balance is encrypted. \nTouch the fingerprint zone to see it.....")
        tvBalanceInfo.setTextColor(Color.RED)

        fingerMgr.authen(
                {
                    tvBalance.setText("$ ${balance}")
                    tvBalanceInfo.setText("")
                },
                {tvBalanceInfo.setText(it)},
                {tvBalanceInfo.setText("failed. Please try again!")},
                {tvBalanceInfo.setText("ERROR! : $it")}
        )
    }

    fun refreshViewL(balance: String) {
        tvBalanceInfo.setText("Your balance is encrypted. \nTouch it to see it.....")
        tvBalanceInfo.setTextColor(Color.RED)

        tvBalance.setOnClickListener{
            tvBalance.setText("$ ${balance}")
        }
    }
}
