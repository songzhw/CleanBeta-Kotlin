package cn.six.payx.presenter

import cn.six.payx.ui.TestRxActivity
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class TestRxPresenter(var view : TestRxActivity) {
    var isMemoryCached = false;
    var isDiskCached = false;
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




    //=================================================
    fun getImage() {
        Observable.concat(memoryCache, diskCache, networkCache)
            .first()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                view.showCacheResult(it)
            }
    }

}