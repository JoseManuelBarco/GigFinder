package com.example.myapplication.chats.data

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("id") val id: Int,
    @SerializedName("chatRoom") val chatRoom: ChatRoom,
    @SerializedName("sender") val sender: Int,
    @SerializedName("content") val content: String,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String
)
