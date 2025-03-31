package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("id") val id: Int,
    @SerializedName("chatRoom") val chatRoom: ChatRoom,
    @SerializedName("sender") val sender: Int,
    @SerializedName("content") val content: String,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String
)

data class ChatRoom(
        @SerializedName("id") val id: Int,
        @SerializedName("encryptionKey") val encryptionKey: String,
        @SerializedName("user1") val user1: User,
        @SerializedName("user2") val user2: User
)

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
