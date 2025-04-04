package com.example.myapplication.objects

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class LoginResponse(
    @SerializedName("access_token")
    val token: String
                        )