package cn.six.payx.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager

private val REQUEST_FINGERPRINT = 120

/*
Note that, since extensions do not actually insert members into classes,
there’s no efficient way for an extension property to have a backing field.
This is why initializers are not allowed for extension properties.
Their behavior can only be defined by explicitly providing getters/setters.
*/
val Activity.fingerMgr : FingerprintManager
    get() = getSystemService(FingerprintManager::class.java)


fun Activity.isFingerprintAvailable() : Boolean {
    val hasPersmission = this.checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED;
    val hasHardware = fingerMgr.isHardwareDetected();
    val hasFingerprints = fingerMgr.hasEnrolledFingerprints();
    if(hasPersmission && hasHardware && hasFingerprints){
        return true
    } else {
        if(! hasPersmission){
            val permissions = arrayOf(Manifest.permission.USE_FINGERPRINT)
            this.requestPermissions(permissions, REQUEST_FINGERPRINT);

            // [!!!] If there’s a member and extension of the same type both applicable to given arguments, a member always wins!
//            this.onRequestPermissionsResult : (...)-> Unit = {
//            }



        } else if(!hasHardware){ // hasPermission && !hasHardware
            showToast("This device doesn't support Fingerprint authentication")
        } else if(!hasFingerprints) { // hasPermssion && hasHardware && !hasFingerprints
            showToast("You haven't enrolled any fingerprint, go to System Settings -> Security -> Fingerprint")
        }
    }
    return false
}

fun Activity.onPermitted(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when(requestCode){
        REQUEST_FINGERPRINT ->{
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isFingerprintAvailable()
            } else {
                showToast("Please give app fingerprint permission")
            }
        }
    }
}

fun FingerprintManager.authen(onSucc : (FingerprintManager.AuthenticationResult?)->Unit,
                              onHelp : (CharSequence?)->Unit,
                              onFail : ()->Unit,
                              onError : (CharSequence?)->Unit){

    this.authenticate(null, null, 0,
            object : FingerprintManager.AuthenticationCallback(){
                override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                    onSucc(result)
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    onHelp(helpString)
                }

                // After getting this, you can retry and touch the fingerprint zone again.
                override fun onAuthenticationFailed() {
                    onFail()
                }

                // "尝试次数过多， 请稍后重试 "
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    onError(errString)
                }
            }
            , null)
}
