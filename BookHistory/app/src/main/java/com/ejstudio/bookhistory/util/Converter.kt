package com.ejstudio.bookhistory.util

import android.content.Context
import android.content.res.Resources
import android.util.Base64
import android.util.TypedValue

import android.util.DisplayMetrics
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object Converter {

    fun pxToDp(context: Context, px: Int): Int {
        val resources: Resources = context.getResources()
        val metrics: DisplayMetrics = resources.getDisplayMetrics()
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
    }

    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.getResources().getDisplayMetrics()).toInt()
    }

    val key = "BookHistory12345"
    var iv = byteArrayOf(
        0x01,
        0x02,
        0x03,
        0x04,
        0x05,
        0x06,
        0x07,
        0x08,
        0x09,
        0x10,
        0x11,
        0x12,
        0x13,
        0x14,
        0x15,
        0x16
    )

    // 사용자 지정 키로 AES256 암호화
    fun encByKey(key: String, value: String): String? {
        return encByKey(key.toByteArray(), value.toByteArray())
    }

    // 사용자 지정 키로 AES256 복호화
    fun encByKey(key: ByteArray?, value: ByteArray?): String? {
        val secretKeySpec = SecretKeySpec(key, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
        val randomKey: ByteArray = cipher.doFinal(value)
        return Base64.encodeToString(randomKey, 0)
    }

    // 사용자 지정 키로 AES256 복호화
    fun decByKey(key: String, plainText: String?): String? {
        return decByKey(key.toByteArray(), Base64.decode(plainText, 0))
    }

    // 사용자 지정 키로 AES256 복호화
    fun decByKey(key: ByteArray?, encText: ByteArray?): String? {
        val secretKeySpec = SecretKeySpec(key, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
        val secureKey: ByteArray = cipher.doFinal(encText)
        return String(secureKey)
    }

}