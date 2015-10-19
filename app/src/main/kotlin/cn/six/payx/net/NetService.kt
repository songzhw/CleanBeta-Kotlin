package cn.six.payx.net

import cn.six.payx.entity.SplashResponse
import retrofit.http.*
import rx.Observable
import java.lang.Deprecated


interface NetService{
    @GET("/mytest/app.html")
    fun getSplashInfo(@Query("msg") msg : String, @Query("sign") sign:String) : Observable<SplashResponse>

    @FormUrlEncoded
    @GET("/mytest/app.html")
    fun getSplashInfo_Post(@Field("msg") msg : String, @Field("sign") sign:String) : Observable<SplashResponse>

    @FormUrlEncoded
    @POST("/mytest/app.html")
    fun getHome(@Field("msg") msg : String, @Field("sign") sign:String) : Observable<HomeResponse>

}