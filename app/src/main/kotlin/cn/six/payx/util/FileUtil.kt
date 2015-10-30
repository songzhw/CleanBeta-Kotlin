package cn.six.payx.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.*

fun savePortrait(photo: Bitmap) {
    val file = File(Environment.getExternalStorageDirectory(), "songzhw.jpg")
    var out = file.outputStream()
    photo.compress(Bitmap.CompressFormat.JPEG, 75, out)
    out.close()
}

fun getPortrait() : Bitmap?{
    val file = File(Environment.getExternalStorageDirectory(), "songzhw.jpg")
    var ret = BitmapFactory.decodeFile(file.absolutePath)
//    println("szw getPortrait() width = ${ret.width} ; height = ${ret.height}")
    return ret
}

