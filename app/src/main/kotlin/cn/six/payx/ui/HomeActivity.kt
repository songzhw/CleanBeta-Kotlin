package cn.six.payx.ui

import android.os.Bundle
import android.support.v7.widget.Toolbar
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import kotlinx.android.synthetic.activity_home.*

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
    }
}
