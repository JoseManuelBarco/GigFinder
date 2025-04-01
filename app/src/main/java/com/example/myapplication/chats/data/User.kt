package com.example.myapplication.chats.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("type") val type: String,
    @SerializedName("avgRating") val avgRating: Double,
    @SerializedName("profileImageIdentifier") val profileImageIdentifier: String
)