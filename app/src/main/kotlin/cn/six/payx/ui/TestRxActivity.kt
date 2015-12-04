package cn.six.payx.ui

import android.os.Bundle
import android.view.View
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.presenter.TestRxPresenter
import kotlinx.android.synthetic.activity_test_rx.*
import kotlin.properties.Delegates


// other occasions that may use RxJava
// * Network (e.g. Retrofit)
// * Complex data handling (map, filter, distince, take, reduce ...)

public class TestRxActivity : BaseActivity(){
    var presenter : TestRxPresenter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_rx)

        presenter = TestRxPresenter(this)
    }

    fun whenCache(v : View){
        tvShow.setText("")
        presenter.getImage()

    }

    fun showCacheResult(method :String, str : String){
        tvShow.setText("${method}() -- ${str}")
    }

    fun multiApi(v : View){
        tvShow.setText("")
        presenter.accessMultiApi()
    }

    fun subsequentApi( v : View){
        tvShow.setText("")
        presenter.subsequentApi()
    }

    fun complexCompute( v : View){
        tvShow.setText("")
        presenter.complexCompute()
    }

}