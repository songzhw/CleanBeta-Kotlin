package cn.six.payx.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.presenter.TestRxPresenter
import cn.six.payx.util.showToast
import kotlinx.android.synthetic.activity_test_rx.*
import rx.android.view.ViewObservable
import rx.android.widget.OnTextChangeEvent
import rx.android.widget.WidgetObservable
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


// other occasions that may use RxJava
// * Network (e.g. Retrofit)
// * Complex data handling (map, filter, distince, take, reduce ...)
// * see : http://blog.csdn.net/lzyzsd/article/details/50120801
public class TestRxActivity : BaseActivity(){
    var presenter : TestRxPresenter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_rx)

        presenter = TestRxPresenter(this)

        // can not click one button more than once in a very short time
        noStickyClick()

        // buffer click
        bufferClick()

        // debounce search
        debounceSearch()

        // limition on the input
        limitInput()

    }

    fun debounceSearch(){
//        WidgetObservable.text(etSearch)
    }

    // 1. You can only input two decimal in this EditText
    // 2. and EditText will add 0 in the first position if you input "." at first
    fun limitInput(){
        WidgetObservable.text(etInputLimit)
            .map { it.text().toString() }
            .subscribe {
                if(it.startsWith(".")){
                    etInputLimit.setText("0$it")
                    etInputLimit.setSelection(it.length+1) //now has a extro "0", so +1
                }
            }

        WidgetObservable.text(etInputLimit)
            .map { it.text().toString() }
            .filter{ str : String ->
                var pointIndex = str.indexOf(".")
                pointIndex >=0
                    && str.substring(pointIndex).length > 3
            }
            .subscribe{ str : String ->
                var pointIndex = str.indexOf(".")
                if(pointIndex >= 0){
                    var ret = str.subSequence(0, pointIndex+3).toString()
                    etInputLimit.setText(ret)
                    etInputLimit.setSelection(ret.length)
                }
            }
    }

    fun noStickyClick(){
        ViewObservable.clicks(btnDoubleClick)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe{
                    println("click btnDoubleClick ! ${System.currentTimeMillis()}")
                }
    }

    fun bufferClick(){
        // "szw click--subscriber" will be triggered every two seconds
        // even if the data is a empty list : "szw click--subscribe []"
        // this may be a bad choice

        /*
        ViewObservable.clicks(btnBufferClick)
            .map{
                println("szw click -- map()")
                " [single] "
            }
            .buffer(2, TimeUnit.SECONDS)
            .subscribe{ data : MutableList<String> ->
                println("szw click -- subscribe $data")
            }
        */


        /*
// 1. no click event. Still trigger subscribe{} every two seconds
19:50.361 I/System.out: szw click -- subscribe []
19:52.351 I/System.out: szw click -- subscribe []

// 2. click event
19:54.161 I/System.out: szw click -- map()
19:54.281 I/System.out: szw click -- map()
19:54.351 I/System.out: szw click -- subscribe [ [single] ,  [single] ]

// 3. another click event after two seconds
19:54.391 I/System.out: szw click -- map()
19:54.531 I/System.out: szw click -- map()
19:54.661 I/System.out: szw click -- map()
19:54.771 I/System.out: szw click -- map()
19:54.901 I/System.out: szw click -- map()
19:55.001 I/System.out: szw click -- map()
19:55.131 I/System.out: szw click -- map()
19:55.251 I/System.out: szw click -- map()
19:55.381 I/System.out: szw click -- map()
19:56.351 I/System.out: szw click -- subscribe [ [single] ,  [single] ,  [single] ,  [single] ,  [single] ,  [single] ,  [single] ,  [single] ,  [single] ]

// 4. no click event.
19:58.351 I/System.out: szw click -- subscribe []
20:00.351 I/System.out: szw click -- subscribe []
        */
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
        tvShow.setText("aaaaaa")
        presenter.complexCompute()

    }



}