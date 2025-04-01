package com.example.myapplication.chats.data

import com.google.gson.annotations.SerializedName

data class RefreshChatRooms (
    @SerializedName("message_type") var messageType: String = "get-chat-rooms"
)