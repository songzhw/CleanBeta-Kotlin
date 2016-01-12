package cn.six.payx.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.entity.BannerItem
import cn.six.payx.presenter.HomePresenter
import cn.six.payx.util.JniUtil
import cn.six.payx.util.getPortrait
import cn.six.payx.util.jump
import cn.six.payx.util.showToast
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.activity_home.*
import rx.android.view.OnClickEvent
import com.jakewharton.rxbinding.view.RxView
import kotlin.properties.Delegates

public class HomeActivity : BaseActivity(){


    var adapter : HomePagerAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var presenter = HomePresenter(this)

        var toolbar = topHome as Toolbar
        toolbar.title = "  Home Page!!"
        toolbar.setNavigationIcon(R.drawable.ic_launcher)


        var portrait = getPortrait()
        if(portrait != null ){
            ivHomeMenuPortrait.setImageBitmap(portrait)
        }

        adapter = HomePagerAdapter(this)
        adapter.data = emptyList()  // error：java.lang.ArithmeticException: divide by zero
        vp_home.adapter = adapter



        vp_home.setOnTouchListener( object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent) : Boolean{
                val currentIndex = vp_home.currentItem
                return presenter.clickBanner(currentIndex, v, event)
            }
        })

        // so clicking the menu, will not trigger the content click event.
        RxView.clicks(llayHomeMenu)
            .subscribe{}

        RxView.clicks(rlayHomeMenuPortrait)
            .subscribe{ev: OnClickEvent ->
                var opt = ActivityOptionsCompat.makeSceneTransitionAnimation(this, ivHomeMenuPortrait, "portraint_show")
                var it = Intent(this, PortraitDetailActivity::class.java)
                ActivityCompat.startActivity(this, it, opt.toBundle())
            }



        RxView.clicks(btnHomeScan)
            .subscribe{
//                IntentIntegrator(this).initiateScan(); // go to the CaptureActivity(landscape)

                // go to the CaptureActivity(portrait orientation)
                var zxing = IntentIntegrator(this)
                zxing.setCaptureActivity(VerticalCaptureActivity::class.java)
                zxing.initiateScan()
            }

        RxView.clicks(tvHomeBankCards)
            .subscribe{
                jump(CardActivity::class.java)
            }

        RxView.clicks(btnHomePayments)
            .subscribe{
                val type  = Random().nextInt(2).toString()
                jump( PaymentsActivity::class.java,  mapOf("showType" to type)  )
            }    

        RxView.clicks(btnHomeBalance)
            .subscribe{
                jump(BalanceActivity::class.java)
            }                    

        // jni test
        println("szw ===============================================")
        println("szw ======" + JniUtil.getKey()  + "====")
        println("szw ======" + JniUtil().getNo() + "====")
        println("szw ===============================================")

//        // cert test
//        var certSign = CertUtil.getSign(this);
//        var publicKey = CertUtil.getPublicKey(certSign);
//        println("szw **** publicKey = ${publicKey} ****");

        presenter.initData()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IntentIntegrator.REQUEST_CODE){
                // 扫码扫到了信息
                val contents = data?.getStringExtra(Intents.Scan.RESULT)
                println("szw zxing result = ${contents}")

                // sometimes, it's the presenter to parse the data, and to do something
            }
        }
    }

    fun notifyMessage(msg : String){
        showToast(msg)
    }

    fun refreshWholePage(banners : List<BannerItem>){
        adapter.data = banners
        adapter.notifyDataSetChanged()

        var firstIndex = adapter.getFirstItemIndex(0)// if(data.size() == 2) then fistIndex == 1000;
        vp_home.setCurrentItem(firstIndex)
    }

    fun clickItemInBanner(args : Map<String, String>){
        jump(WebViewActivity::class. java, args)
    }


}
