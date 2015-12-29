package cn.six.payx.presenter

import android.content.Intent
import android.provider.Settings
import cn.six.payx.ui.BalanceActivity
import cn.six.payx.util.isFingerprintAvailable

public class BalanceMPresenter(var view : BalanceActivity) : IBalancePresenter {


    override fun init(){
        // if avaiable, onResume() will authenticate the fingerprint
        // otherwise, we will have to go to set a fingerprint
        if(!view.isFingerprintAvailable()){
            // has no fingerprints yet. Go to set it
            val it = Intent(Settings.ACTION_SECURITY_SETTINGS)
            view.startActivity(it)


            // 01. Way One:
            // view.finish()

            // 02. Way Two:
            // or onResume(), onStop() to handle the going back situation
        }


    }

}
