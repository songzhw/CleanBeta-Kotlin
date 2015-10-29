package cn.six.payx.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.util.dp2px
import cn.six.payx.util.showToast
import kotlinx.android.synthetic.activity_portrait.*
import rx.android.view.ViewObservable

public class PortraitDetailActivity : BaseActivity() {
    val REQ_CODE_TAKE_PICTURE = 21
    val REQ_CODE_PICK_PIC_FROM_ALBUM = 22 
    val REQ_CODE_CROP_IMAGE = 23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portrait)

        //Pick the portrait picture from the Album
        ViewObservable.clicks(tvPortraitGallery)
            .subscribe{
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    try {
                        val intent = Intent(Intent.ACTION_PICK, null)
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                        startActivityForResult(intent, REQ_CODE_PICK_PIC_FROM_ALBUM)
                    } catch (e: ActivityNotFoundException) {
                        showToast("Found no album！")
                    }

                } else {
                    showToast("SD card is error, and can not access the album！")
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode != Activity.RESULT_OK || intent == null) {
            return
        }

        // read the picture from the album, and go to crop it
        if(requestCode == REQ_CODE_PICK_PIC_FROM_ALBUM){
            startPhotoZoom(intent?.data)
        }

        // deal with the croped image
        else if(requestCode == REQ_CODE_CROP_IMAGE) {
            var extras = intent.extras
            var bitmap : Bitmap = extras.getParcelable("data")
            ivPortraitShow.setImageBitmap(bitmap)
        }

    }

    private fun startPhotoZoom(uri: Uri?) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", dp2px(this, 60f))
        intent.putExtra("outputY", dp2px(this, 60f))
        intent.putExtra("return-data", true)
        startActivityForResult(intent, REQ_CODE_CROP_IMAGE)
    }

}