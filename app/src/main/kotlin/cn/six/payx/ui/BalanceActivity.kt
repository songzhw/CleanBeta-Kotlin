package cn.six.payx.ui

import android.graphics.Color
import android.os.Bundle
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.util.authen
import cn.six.payx.util.fingerMgr
import cn.six.payx.util.onPermitted
import kotlinx.android.synthetic.activity_balance.*

public class BalanceActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        tvBalanceInfo.setText("Your balance is encrypted. \nTouch the fingerprint zone to see it.....")
        tvBalanceInfo.setTextColor(Color.RED)

        fingerMgr.authen(
                {
                    tvBalance.setText("$88.88")
                    tvBalanceInfo.setText("")
                },
                {tvBalanceInfo.setText(it)},
                {tvBalanceInfo.setText("failed. Please try again!")},
                {tvBalanceInfo.setText("ERROR! : $it")}
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        onPermitted(requestCode, permissions, grantResults)
    }
}
