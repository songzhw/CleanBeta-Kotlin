package cn.six.payx.net

import retrofit.RestAdapter
import retrofit.android.AndroidLog

object RetrofitSingleton {
    fun getNetService() : NetService{
        var restAdapter = RestAdapter.Builder()
                .setEndpoint("https://xxx.xxx.xxx.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(AndroidLog("szw"))
                .build();

        return restAdapter.create(NetService::class.java);
    }
}

