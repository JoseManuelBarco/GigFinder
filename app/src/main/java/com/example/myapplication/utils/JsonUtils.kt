package com.example.myapplication.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime


object JsonUtils {
     @RequiresApi(Build.VERSION_CODES.O)
     val gson =  GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    @RequiresApi(Build.VERSION_CODES.O)
    fun <T> encode(obj: T): String {
        return gson.toJson(obj)
    }

     @RequiresApi(Build.VERSION_CODES.O)
     inline fun <reified T> decode(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun getMessageType(msg: String?): String? {
        try {
            val gson = Gson()
            val jsonObject = JsonParser.parseString(msg).asJsonObject

            // Get message_type
            val messageType = jsonObject["message_type"].asString
            return messageType
        } catch (e: Exception) {
            return null
        }
    }
}