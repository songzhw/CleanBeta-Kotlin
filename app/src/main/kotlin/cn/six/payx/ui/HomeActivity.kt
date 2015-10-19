package cn.six.payx.ui

import android.os.Bundle
import android.support.v7.widget.Toolbar
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.entity.HomeResponse
import cn.six.payx.net.BaseRequest
import cn.six.payx.net.RetrofitSingleton
import cn.six.payx.util.showToast
import kotlinx.android.synthetic.activity_home.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class HomeActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var toolbar = topHome as Toolbar
        toolbar.title = "  Home Page!!"
        toolbar.setNavigationIcon(R.drawable.ic_launcher)

        var adapter = HomePagerAdapter(this)
        adapter.data = emptyList()  // error：java.lang.ArithmeticException: divide by zero
        vp_home.adapter = adapter

        var raw = hashMapOf("action" to "getHomeInfo")
        var req = BaseRequest(raw)

        RetrofitSingleton.getNetService()
            .getHome(req.msg, req.sign)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    {resp : HomeResponse ->
                        adapter.data = resp.banners
                        adapter.notifyDataSetChanged()
                    },
                    {error -> showToast(error.getMessage().toString()) }
            )
    }
}
