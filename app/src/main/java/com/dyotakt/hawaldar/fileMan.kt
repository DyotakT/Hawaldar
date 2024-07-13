package com.dyotakt.hawaldar

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class fileMan {

    fun readPrivateFile(applicationContext: Context?): String {
        val FILE_NAME = "sensitive_info.txt"
        val contents =
            File(applicationContext!!.filesDir, FILE_NAME).bufferedReader().useLines { lines ->
                lines.fold("") { working, line ->
                    "$working\n$line"
                }
            }
        return contents
    }


    fun createPrivateFile(applicationContext: Context?) { //this is not wrong. it is suggested not to use the activity context in viewmodel as VM lives longer. Here, we are using ApplicationContext
        val FILE_NAME = "sensitive_info.txt"
        Log.d("FILE CONTENT", addToFile())
        val fileContents = addToFile()
        if (applicationContext != null) {
            File(applicationContext.filesDir, FILE_NAME).bufferedWriter().use { writer ->
                writer.write(fileContents)
            }
        }
    }

    fun addToFile(): String {
        val userData = listOf(
            items(
                "Steam",
                (Math.floor(Math.random() * 100000)).toString(),
                "Testing123",
                "023026033",
                "steam",
                123456
            ),
            items(
                "Google",
                (Math.floor(Math.random() * 100000)).toString(),
                "Testing1234",
                "234234234",
                "google",
                123456
            )
        )
        val json = Json.encodeToString(userData)
        return json
    }
}