package cn.six.payx.net

import cn.six.payx.util.DigestUtil
import cn.six.payx.util.PayxBase64
import org.json.JSONObject
import java.util.*
import kotlin.properties.Delegates

class BaseRequest(var args: HashMap<String, out Any>) {
    var json = JSONObject()
    var msg : String by Delegates.notNull()
    var sign : String by Delegates.notNull()

    init {
//        json.put("action", "getBootImg");
        json.put("login_id", "id====");
        json.put("login_token", "token===");
        json.put("mobile_os_type", "android");
        json.put("client_version", "0.1");
        var df = java.text.SimpleDateFormat("yyyyMMddHHmmss")
        json.put("request_time", df.format(Date()));

        for ((k, v) in args) {
            json.put(k, v)
        }

        msg = PayxBase64.encode(json.toString().toByteArray("UTF-8"))
        sign = DigestUtil.digest(msg,"some random key")
    }

}