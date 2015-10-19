package cn.six.payx.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import kotlinx.android.synthetic.activity_webview.*

public class WebViewActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        println("szw WebViewActivity  000")

        var settings = webview.settings
        settings.javaScriptEnabled = true
        settings.saveFormData = false
        settings.savePassword = false
        settings.setSupportZoom(false)

        // remove the old, dangerous JavaScript bridge
        if (android.os.Build.VERSION.SDK_INT > 10) {
            webview.removeJavascriptInterface("accessibility");
            webview.removeJavascriptInterface("accessibilityTraversal");
            if (android.os.Build.VERSION.SDK_INT < 17) {
                webview.removeJavascriptInterface("searchBoxJavaBridge_");
            }
        }


        // load cookie
//        var rawCookieValue = getIntent().getStringExtra("intent_webview_cookie")
//        var cookie = "Key1="+rawCookieValue+";key2=app_android"
//        setCookie(url, cookie)


        var url = getIntent().getStringExtra("intent_webview_url")
        println("szw WebViewActivity url = "+url)
        webview.loadUrl(url)
    }

    override fun onDestroy() {
        //加上这句， 以避免日志中出现error: WebView.destroy() called while still attached!
        rlayWebViewAll.removeAllViews()
        //调用webview.destory()的原因，是可能：“WebView页面中播放了音频,退出Activity后音频仍然在播放”
        if (webview != null) {
            webview.removeAllViews()
            webview.destroy()
        }
        super.onDestroy()
    }


    private fun setCookie(url: String, cookie: String) {
        // CookieSyncManger is drprecated since API Level 21
        // WebView will automatically syncs cookies as necessary.

        CookieSyncManager.createInstance(this.applicationContext)
        val cookieManager = CookieManager.getInstance()

        cookieManager.setAcceptCookie(true)
        cookieManager.removeSessionCookie()

        cookieManager.setCookie(url, cookie)
        CookieSyncManager.createInstance(this.applicationContext)
        CookieSyncManager.getInstance().sync()
    }



}