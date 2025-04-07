package com.example.myapplication.api_objects

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val token: String
                        )