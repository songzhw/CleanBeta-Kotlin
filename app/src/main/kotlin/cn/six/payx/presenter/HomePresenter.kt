package cn.six.payx.presenter

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import cn.six.payx.entity.BannerItem
import cn.six.payx.entity.HomeResponse
import cn.six.payx.net.BaseRequest
import cn.six.payx.net.RetrofitSingleton
import cn.six.payx.ui.HomeActivity
import cn.six.payx.ui.WebViewActivity
import cn.six.payx.util.jump
import cn.six.payx.util.showToast
import kotlinx.android.synthetic.activity_home.vp_home
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.properties.Delegates

public class HomePresenter (var view : HomeActivity){
    var bannersData : List<BannerItem> by Delegates.notNull()

    //for clicking viewpager item
    private var oldX = 0f
    private var newX = 0f
    private val clickSensibleScope = 10f

    fun initData(){
        var raw = hashMapOf("action" to "getIndexInfo")
        var req = BaseRequest(raw)

        RetrofitSingleton.getNetService()
                .getHome(req.msg, req.sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {resp : HomeResponse ->
                            if(!resp.isSucc()){
                                view.notifyMessage(resp.retdesc)
                            } else{
                                bannersData = resp.banners
                                view.refreshWholePage(bannersData)
                            }
                        },
                        {error -> view.notifyMessage(error.message.toString()) }
                )
    }

    fun clickBanner(currentIndex : Int, v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> oldX = event.x

            MotionEvent. ACTION_UP -> {
                newX = event. x
                if (Math.abs( oldX - newX) < clickSensibleScope) {
                    val realIndex = currentIndex % bannersData .size
                    var item = bannersData.get(realIndex)
                    var args = mapOf<String, String>("intent_webview_url" to item. bannerUrl)

                    view.clickItemInBanner(args)
                    return true
                }
                oldX = 0f
                newX = 0f
            }

        }
        return false
    }

}