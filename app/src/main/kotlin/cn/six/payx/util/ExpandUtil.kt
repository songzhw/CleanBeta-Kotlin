package cn.six.payx.util

import android.widget.EditText
import android.widget.Toast
import cn.six.payx.core.BaseApp


fun showToast(text : String){
    Toast.makeText(BaseApp.appContext, text, Toast.LENGTH_SHORT).show()
}

fun EditText.string() : String {
    return this.getText()?.toString() ?: "" //this是指EditText对象。因为这是猴子补丁
}