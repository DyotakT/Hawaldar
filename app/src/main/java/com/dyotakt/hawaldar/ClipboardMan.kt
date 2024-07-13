package com.dyotakt.hawaldar

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE

class ClipboardMan {
    val clipboardManager =  context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    fun addToClipboard(otp: Int) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("OTP", otp.toString()))
    }
}