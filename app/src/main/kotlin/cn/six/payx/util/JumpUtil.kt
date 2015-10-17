package cn.six.payx.util

import android.app.Activity
import android.content.Intent

fun Activity.jump(clz : Class<out Any>){
    val it = Intent(this, clz)
    this.startActivity(it)
}