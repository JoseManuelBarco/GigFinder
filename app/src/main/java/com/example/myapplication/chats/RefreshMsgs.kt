package com.example.myapplication.chats

interface RefreshMsgs {
    fun refreshMessages()
    fun runOnUiThread(function: () -> Unit)
}
