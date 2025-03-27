package com.example.myapplication.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class RefreshChats(
    @SerializedName("message_type") val messageType: String,
    @SerializedName("date") val date: LocalDateTime
)

