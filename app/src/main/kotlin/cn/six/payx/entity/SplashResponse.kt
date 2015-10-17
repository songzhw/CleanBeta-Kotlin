package cn.six.payx.entity

//data class SplashResponse(var retcode: String, var retdesc: String, var bootImageUrl : String) {}

data class SplashResponse(var code: String, var msg: String,
                     var splashUrl: String) {
    fun isSucc(): Boolean = (code == "0000")
}