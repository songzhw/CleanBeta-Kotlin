package cn.six.payx.test

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import kotlin.properties.ReadOnlyProperty


fun broadcaster(init: (Context, Intent?) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        public override fun onReceive(context: Context, intent: Intent?) {
            init(context, intent)
        }
    }
}

fun View.find<T : View> (id: Int): T? = findViewById(id) as? T

fun View.toggleVisibility(): Unit {
    if (visibility == View.VISIBLE){
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
    }
}