package cn.six.payx.core;

import android.support.test.espresso.IdlingResource;

/**
 * @author songzhw
 * @date 2015/9/9
 * Copyright 2015 Six. All rights reserved.
 */
public class AsyncIdlingRes implements IdlingResource {

    private ResourceCallback resourceCallback;
    private IIdlingFlag flag;

    public AsyncIdlingRes(IIdlingFlag flag) {
        this.flag = flag;
    }

    public void removeListener(){
        flag = null;
    }

    @Override
    public String getName() {
        return "AsyncIdlingRes" ;
    }
    @Override
    public boolean isIdleNow() {
        boolean isIdleNow = flag.isFinish();
        if(isIdleNow){
            resourceCallback.onTransitionToIdle();
        }
        return isIdleNow;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }


}
