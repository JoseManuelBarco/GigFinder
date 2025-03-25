package com.example.myapplication.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ChatMessage(
    @SerializedName("message_type") val messageType: String,
    @SerializedName("chat_id") val chatId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("date") val date: Date


)
