package com.example.myapplication.chats.data

import com.google.gson.annotations.SerializedName

data class ChatRoom(
    @SerializedName("id") val id: Int,
    @SerializedName("encryptionKey") val encryptionKey: String,
    @SerializedName("user1") val user1: User,
    @SerializedName("user2") val user2: User
)