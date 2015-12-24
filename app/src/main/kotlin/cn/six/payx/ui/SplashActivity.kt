package cn.six.payx.ui

import android.os.Bundle
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.entity.SplashResponse
import cn.six.payx.net.BaseRequest
import cn.six.payx.net.RetrofitSingleton
import cn.six.payx.util.jump
import cn.six.payx.util.showToast
import com.nostra13.universalimageloader.core.ImageLoader

import kotlinx.android.synthetic.activity_splash.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

public class SplashActivity : BaseActivity() {
    val jumpObservable : BehaviorSubject<Void> = BehaviorSubject.create()
    var sub1 : Subscription? = null
    var sub2 : Subscription? = null
    var isFinishedSplash = false
    val hello : String by lazy{
        "str"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val orginStr = this.getString(R.string.splash_copyright)
        tv_splash_copyright.setText(orginStr.format(year))


        var raw = hashMapOf("action" to "splashUrl")
        var req = BaseRequest(raw)

        var netService = RetrofitSingleton.getNetService()
        sub1 = netService.getSplashInfo_Post(req.msg,req.sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { resp : SplashResponse ->
                            var imageLoader = ImageLoader.getInstance()
                            imageLoader.displayImage(resp.splashUrl, iv_splash_ad)
                            jumpObservable.onNext(null) // go to the home page
                            jumpObservable.onCompleted()
                         },
                        {error ->
                            showToast(error.getMessage().toString())
                        })

        sub2 = jumpObservable.asObservable()
            .delay(2, TimeUnit.SECONDS)
            .subscribe{
                isFinishedSplash = true
                jump(UnlockActivity::class.java)
                this.finish()
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        sub1?.unsubscribe()
        sub2?.unsubscribe()
    }
}
