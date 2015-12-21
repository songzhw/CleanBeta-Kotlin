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
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

public class SplashActivity : BaseActivity() {
    val jumpObservable : BehaviorSubject<Void> = BehaviorSubject.create()
    var isFinishedSplash = false

    // show how to delegate a lazy member
    val hello : String by lazy{
        "hello kotlin" // resource.getString(R.string.hello)
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
        netService.getSplashInfo_Post(req.msg,req.sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { resp : SplashResponse ->
                            var imageLoader = ImageLoader.getInstance()
                            imageLoader.displayImage(resp.splashUrl, iv_splash_ad)
                            jumpObservable.onNext(null) // go to the home page
                         },
                        {error ->
                            showToast(error.getMessage().toString())
                        })

        jumpObservable.asObservable()
            .delay(2, TimeUnit.SECONDS)
            .subscribe{
                isFinishedSplash = true
                jump(UnlockActivity::class.java)
                this.finish()
            }

    }
}
