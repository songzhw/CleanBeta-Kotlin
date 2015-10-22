package cn.six.payx.entity

import java.util.*

data class HomeResponse(var code: String, var msg: String,
                        var banners : ArrayList<BannerItem>){
    fun isSucc() = (code == "succ")
}