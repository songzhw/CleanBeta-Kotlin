package cn.six.payx.test

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ListView
import cn.six.payx.core.BaseActivity

public class TestBase : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var btn = Button(this)
        setContentView(btn)

        btn.text = "Click Me !"
        // 1. SAM conversions
        btn.setOnClickListener { v: View ->
            println("szw click ${v}")
        }

        // 2. OnTouchListener
        var oldX : Float
        btn.setOnTouchListener { v: View, event: MotionEvent ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    println("szw action_down")
                    oldX = event.x
                }
                MotionEvent.ACTION_UP -> {
                    println("szw action_up")
                    true
                }
            }
            false
        }


        // 3. Broadcast Receiver
        var receiver = broadcaster { context, intent ->
            var isOkay = intent!!.getBooleanExtra("is_okay", false)
            if (isOkay) {
                println("szw receiver")
            }
        }
//        this.registerReceiver(receiver, intentFileter)



        // 4.  AdapterView  (SAM conversion)
        var alist = ListView(this)
        alist.setOnItemClickListener { adapterView, view, i, l ->
            // ....
        }
    }
}