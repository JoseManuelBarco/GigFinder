package com.example.myapplication.chats.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class RefreshChats(
    @SerializedName("message_type") val messageType: String,
    @SerializedName("date") val date: LocalDateTime
)

