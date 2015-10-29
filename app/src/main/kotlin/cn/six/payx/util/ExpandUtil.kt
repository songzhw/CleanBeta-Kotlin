package cn.six.payx.util

import android.app.ActivityManager
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import cn.six.payx.core.BaseApp


fun showToast(text : String){
    Toast.makeText(BaseApp.appContext, text, Toast.LENGTH_SHORT).show()
}

fun getProcessName(context: Context): String {
    val pid = android.os.Process.myPid()
    val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (appProcess in mActivityManager.getRunningAppProcesses()) {
        if (appProcess.pid === pid) {
            return appProcess.processName
        }
    }
    return ""
}

fun EditText.string() : String {
    return this.getText()?.toString() ?: "" //this是指EditText对象。因为这是猴子补丁
}

fun dp2px(context: Context, dip: Float): Int {
    return (context.resources.displayMetrics.density * dip).toInt()
}
