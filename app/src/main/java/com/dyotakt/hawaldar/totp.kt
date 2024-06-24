package com.dyotakt.hawaldar

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


class totp {

    fun GenerateOTP(data: String, key: String): Int {
        var hmacHash: ByteArray = GenerateHash(data,key).toByteArray()
        var offset: Int = (hmacHash[39] and 0xf).toInt()
        var truncatedHash =
            (hmacHash[offset++].toInt() and 0x7f shl 24) or (hmacHash[offset++].toInt() and 0xff shl 16) or (hmacHash[offset++].toInt() and 0xff shl 8) or (hmacHash[offset++].toInt() and 0xff)
        return (truncatedHash%1000000)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun GenerateHash(data: String, key: String): String {
        var key2 = (System.currentTimeMillis()/30000).toString()
        var secretKey = SecretKeySpec(key2.toByteArray(Charsets.UTF_8), "HmacSHA1")
        var mac: Mac = Mac.getInstance("HmacSHA1")
        mac.init(secretKey)
        return mac.doFinal(data.toByteArray(Charsets.UTF_8)).toHexString()
    }
}