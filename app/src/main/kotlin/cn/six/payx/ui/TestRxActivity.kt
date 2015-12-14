package cn.six.payx.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.presenter.TestRxPresenter
import cn.six.payx.util.showToast
import kotlinx.android.synthetic.activity_test_rx.*
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.android.view.ViewObservable
import rx.android.widget.OnTextChangeEvent
import rx.android.widget.WidgetObservable
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


// other occasions that may use RxJava
// * Network (e.g. Retrofit)
// * Complex data handling (map, filter, distince, take, reduce ...)
// * see : http://blog.csdn.net/lzyzsd/article/details/50120801

/*

*/
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

        // form validation
        validForm()

        // calculate the result
        calculateFormResult()
    }

    fun calculateFormResult(){
        var et1Valid = WidgetObservable.text(etSearch)
                .map{ it. text().toString()}
        var et2Valid = WidgetObservable.text(etInputLimit)
                .map{ it. text().toString()}

        Observable.combineLatest(et1Valid, et2Valid){s1, s2 ->
            try{
                s1.toInt() + s2.toInt()
            } catch (e : Exception){
                " -- "
            }
        }.subscribe{
            tvFormResult.setText("$it")
        }

        /*
        Another way (is not as good as the combineLatest()):

        var publisher: PublishSubject<Float> = PublishSubject.create<Float>();
        publisher.asObservable().subscribe{
            tvFormResult.setText("$it")
        };

        var et3Valid = WidgetObservable.text(etSearch)
                .map{ it. text().toString().toFloat()}
                .subscribe {publisher.onNext(s1.Float() + s2.Float())}
        var et4Valid = WidgetObservable.text(etInputLimit)
                .map{ it. text().toString().toFloat()}
                .subscribe {publisher.onNext(s1.Float() + s2.Float())}

        */
    }

    fun validForm(){
        var et1Valid = WidgetObservable.text(etSearch)
            .map{ it. text().toString()}
        var et2Valid = WidgetObservable.text(etInputLimit)
                .map{ it. text().toString()}

        Observable.combineLatest(et1Valid, et2Valid){s1, s2 ->
            !TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)
        }.subscribe{
            btnForm01.isEnabled = it
        }
    }

    /**
    A typical example of this is instant search result boxes. As you type the word "Bruce Lee",
    you don't want to execute searches for B, Br, Bru, Bruce, Bruce, Bruce L ... etc.
    But rather intelligently wait for a couple of moments, make sure the user has finished typing the whole word,
    and then shoot out a single call for "Bruce Lee".
     * */
    fun debounceSearch(){
        WidgetObservable.text(etSearch)
            .debounce(400, TimeUnit.MILLISECONDS)
            .map{ it.text().toString()}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({println("szw onNext($it)")},
                    {println("szw onError($it)")},
                    {println("szw onComplete()")})
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
//                .debounce(2, TimeUnit.SECONDS)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe{
                    println("szw click btnDoubleClick ! ${System.currentTimeMillis()}")
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


    var count : Int = 0
    fun clickPolling( v : View){
        count = 0
        var onSubp = Observable.OnSubscribe<String>() {
            Schedulers.newThread()
                .createWorker()
                .schedulePeriodically( //(action, long initialDelay, long period, TimeUnit unit)
                        {it.onNext(increseCount())},
                         0, 1, TimeUnit.SECONDS
                )
        }
        var observable = Observable.create(onSubp)
        var subp = observable.take(10) // or filter() to depends on an outter variable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ tvShow.setText("Polling $it")}


        var subscriptions = CompositeSubscription()
        subscriptions.add(subp);


    }

    fun increseCount() : String{
        count ++
        return count.toString()
    }


    override fun onDestroy() {
        super.onDestroy()
        // TODO
        // [onCreate]  subscription = Observable.just(..);
        // [onDestory] subscription.unsubscribe();
    }

}