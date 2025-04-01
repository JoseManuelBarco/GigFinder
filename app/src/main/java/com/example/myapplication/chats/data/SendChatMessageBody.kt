package com.example.myapplication.chats.data

import com.google.gson.annotations.SerializedName

data class SendChatMessageBody(
    @SerializedName("message_type") val messageType: String,
    @SerializedName("chat_id") val chatId: Int,
    @SerializedName("content") val content: String,
)
