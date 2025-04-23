package com.example.myapplication.helpers

import android.content.Context
import com.example.myapplication.objects.User
import com.google.gson.Gson

class PreferencesHelper(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveCredentials(email: String, password: String) {
        with(sharedPreferences.edit()) {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    fun getEmail(): String? = sharedPreferences.getString("email", null)
    fun getPassword(): String? = sharedPreferences.getString("password", null)

    fun saveUserProfile(user: User) {
        val userJson = gson.toJson(user)
        with(sharedPreferences.edit()) {
            putString("userProfile", userJson)
            apply()
        }
    }

    fun getUserProfile(): User? {
        val userJson = sharedPreferences.getString("userProfile", null)
        return userJson?.let {
            gson.fromJson(it, User::class.java)
        }
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}