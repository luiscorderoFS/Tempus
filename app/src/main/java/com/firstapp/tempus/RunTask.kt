package com.firstapp.tempus

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class RunTask(private val link: String) : Runnable {

    var result: String = ""

    override fun run() {
        handleLink()
    }

    private fun handleLink() {
        val url = URL(link)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val statusCode = connection.responseCode
        if (statusCode == HttpURLConnection.HTTP_OK) {
            Log.v("asdf", "reading")
            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }
            val jsonString = stringBuilder.toString()
            val jsonObject = JSONObject(jsonString)
            val arrayRows = jsonObject.getJSONArray("rows")
            val objectRows = arrayRows.getJSONObject(0)
            val arrayElements = objectRows.getJSONArray("elements")
            val objectElements = arrayElements.getJSONObject(0)
            val objectDuration = objectElements.getJSONObject("duration")

            result = objectDuration.getString("value")
        }
    }
}