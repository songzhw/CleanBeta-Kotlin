package cn.six.payx.util

import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec

/**
 * @author songzhw
 * @date 2015/11/4
 * Copyright 2015 Six. All rights reserved.
 */

object AES {

    // the returned value is byte[].  So we still have to use "String rawString = Base64.encode( AES.encode(plaintText, secret) );"
    // the "rawString" is the result that you should give to the server
    fun encode(content: String, passkey: String): ByteArray? {
        try {
            val raw = passkey.toByteArray()
            val skeySpec = SecretKeySpec(raw, "AES")
            // iOS use ECB|PKCS7Padding, which is Okay. Server(java) can also decode the result.
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
            return cipher.doFinal(content.toByteArray("utf-8"))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null
    }

}