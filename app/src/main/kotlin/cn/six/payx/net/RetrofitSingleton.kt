package cn.six.payx.net

import retrofit.RestAdapter
import retrofit.android.AndroidLog

object RetrofitSingleton {
	// default : use HttpUrlConnection(src: Platform.java # defaultClient() )
	// so I still have to check the hostName and CA cert
	// of course, a simpler way is using OkHttp as a client of Retrofit
    fun getNetService() : NetService{
        var restAdapter = RestAdapter.Builder()
                .setEndpoint("https://xxx.xxx.xxx.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(AndroidLog("szw"))
                .build();

        return restAdapter.create(NetService::class.java);
    }
}

