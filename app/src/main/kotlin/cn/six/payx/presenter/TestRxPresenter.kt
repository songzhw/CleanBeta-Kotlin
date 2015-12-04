package cn.six.payx.presenter

import cn.six.payx.ui.TestRxActivity
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class TestRxPresenter(var view : TestRxActivity) {
    var isMemoryCached = true;
    var isDiskCached = true;
    var isNetworkCached = true;

    var memoryCache = Observable.create<String>{
        if(isMemoryCached){  // if(memoryCache.isNotEmpty())
            it.onNext("cache from memory")
        } else {
            it.onCompleted()
        }
    }

    var diskCache = Observable.create<String> {
        if(isDiskCached){
            it.onNext("cache from disk")
        } else {
            it.onCompleted()
        }
    }

    var networkCache = Observable.just("Get data from server")

    fun getImage() {
        Observable.concat(memoryCache, diskCache, networkCache)
            .first()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                view.showCacheResult("getImage", it)
            }
    }

    //=================================================
    var api1 = Observable.create<String>{
        SystemClock.sleep(2000);
        it.onNext("access api one")
    }

    var api2 = Observable.create<String>{
        SystemClock.sleep(1000);
        it.onNext("access api two")
    }

    fun accessMultiApi(){
        Observable.merge(api1, api2)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe {
                // TODO
                // this code below will be called twice (api1, api2).
                // what if I want to only called by all apis are finished?
                Log.d("szw", "trace -- accessMultiApi(${it})")
                view.showCacheResult("multiApi",it)
            }
    }

    //=================================================

    fun subsequentApi(){
//        var service : NetService = RetrofitSingleton.getNetService()
//        service.getSplashInfo_Post("msg1","sign1")
//            .flatMap{ service.getHome("msg2","sign2")}
//            .subscribe{
//                //...
//            }
    }


    //=================================================
    fun complexCompute(){
        Observable.just("3","4","3","2","3","5")
            .map{str : String -> str.toInt()}
            .filter{ it > 2}
            .distinct()
            .take(2)
            .reduce{i1, i2 -> i1+i2}
            .subscribe{ view. showCacheResult("complexCompute", "${it}")}
        //=> 3,4,3,3,5
        //=> 3,4,5
        //=> 7
    }

}