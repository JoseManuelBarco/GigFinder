package com.example.myapplication.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonUtils {
     val gson = Gson()

    fun <T> encode(obj: T): String {
        return gson.toJson(obj)
    }

     inline fun <reified T> decode(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }
}