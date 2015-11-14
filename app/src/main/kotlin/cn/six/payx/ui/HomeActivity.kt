package cn.six.payx.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.entity.BannerItem
import cn.six.payx.entity.HomeResponse
import cn.six.payx.net.BaseRequest
import cn.six.payx.net.RetrofitSingleton
import cn.six.payx.util.jump
import cn.six.payx.util.showToast
import kotlinx.android.synthetic.activity_home.*
import rx.android.schedulers.AndroidSchedulers
import rx.android.view.OnClickEvent
import rx.android.view.ViewObservable
import rx.schedulers.Schedulers
import kotlin.properties.Delegates
import cn.six.payx.util.JniUtil

public class HomeActivity : BaseActivity(){
    //for clicking viewpager item
    private var oldX = 0f
    private var newX = 0f
    private val clickSensibleScope = 10f

    private var banners : List<BannerItem> by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var toolbar = topHome as Toolbar
        toolbar.title = "  Home Page!!"
        toolbar.setNavigationIcon(R.drawable.ic_launcher)

        var portrait = getPortrait()
        if(portrait != null ){
            ivHomeMenuPortrait.setImageBitmap(portrait)
        }

        var adapter = HomePagerAdapter(this)
        adapter.data = emptyList()  // error：java.lang.ArithmeticException: divide by zero
        vp_home.adapter = adapter

        var raw = hashMapOf("action" to "getIndexInfo")
        var req = BaseRequest(raw)

        RetrofitSingleton.getNetService()
            .getHome(req.msg, req.sign)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    {resp : HomeResponse ->
                        if(!resp.isSucc()){
                            showToast(resp.msg)
                        } else{
                            banners = resp.banners
                            adapter.data = resp.banners
                            adapter.notifyDataSetChanged()

                            var firstIndex = adapter.getFirstItemIndex(0)// if(data.size() == 2) then fistIndex == 1000;
                            vp_home.setCurrentItem(firstIndex)
                        }
                    },
                    {error -> showToast(error.getMessage().toString()) }
            )

        vp_home.setOnTouchListener( object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> oldX = event.x

                    MotionEvent. ACTION_UP -> {
                        newX = event. x
                        if (Math.abs( oldX - newX) < clickSensibleScope) {
                            val currentIndex = vp_home.currentItem
                            val realIndex = currentIndex % banners .size()
                            var item = banners.get(realIndex)
                            var args = mapOf<String, String>("intent_webview_url" to item. bannerUrl)
                            jump(WebViewActivity::class. java, args)
                            return true
                        }
                        oldX = 0f
                        newX = 0f
                    }

                }
                return false
            }
        })

        // so clicking the menu, will not trigger the content click event.
        ViewObservable.clicks(llayHomeMenu)
            .subscribe{}

        // the 5.x and 6.x will have a scene transition animation. 4.x has not.
        ViewObservable.clicks(rlayHomeMenuPortrait)
            .subscribe{ev: OnClickEvent ->
                var opt = ActivityOptionsCompat.makeSceneTransitionAnimation(this, ivHomeMenuPortrait, "portraint_show")
                var it = Intent(this, PortraitDetailActivity::class.java)
                ActivityCompat.startActivity(this, it, opt.toBundle())
            }


        ViewObservable.clicks(btnHomeScan)
            .subscribe{
                //IntentIntegrator(this).initiateScan(); // go to the CaptureActivity(landscape)

                // go to the CaptureActivity(portrait orientation)
                var zxing = IntentIntegrator(this)
                zxing.setCaptureActivity(VerticalCaptureActivity::class.java)
                zxing.initiateScan()
            }


        // jni test
        println("szw ==================")
        println("szw ======" + JniUtil.getKey() + "====")
        println("szw ======" + JniUtil().getNo() + "====")
        println("szw ==================")        

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IntentIntegrator.REQUEST_CODE){
                // zxing succ
                val contents = data?.getStringExtra(Intents.Scan.RESULT)
                println("szw zxing result = ${contents}")
            }
        }
    }

}
