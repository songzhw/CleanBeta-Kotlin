package cn.six.payx.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.presenter.BalanceLPresenter
import cn.six.payx.presenter.BalanceMPresenter
import cn.six.payx.presenter.IBalancePresenter
import cn.six.payx.util.*
import kotlinx.android.synthetic.activity_balance.*
import kotlin.properties.Delegates


// Fingerprint with Kotlin
// 1. cannot override the existing method in a system class. Members always win!
// 2. fingerMgr will be a lazy property. (the crash log tells me).
public class BalanceActivity : BaseActivity(){
    var presenter : IBalancePresenter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        if(Build.VERSION.SDK_INT >= 23 && this.hasFingerprintHardware){
            presenter = BalanceMPresenter(this)
        } else { // (SDK_INT < 23) || (>=23 but has no fingerprint sensor)
            presenter = BalanceLPresenter(this)
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

    override fun onResume() {
        super.onResume()
        if(Build.VERSION.SDK_INT >= 23 && this.hasFingerprintHardware && this.hasFingerprints){
            refreshViewM(presenter.model)
        }

    }

    override fun onStop() {
        super.onStop()
        if(Build.VERSION.SDK_INT >= 23 && this.hasFingerprintHardware){
            tvBalance.setText("$ **.**")
            tvBalanceInfo.setText("Your balance is encrypted. \nTouch the fingerprint zone to see it.....")
        }

    }
}
