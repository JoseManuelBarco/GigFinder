package com.example.myapplication.chats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.constants.SocketMessageTypes
import com.example.myapplication.chats.data.ChatMessage
import com.example.myapplication.chats.data.SendChatMessageBody
import com.example.myapplication.chats.data.RefreshChats
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ChatService {
    private var messagesList: MutableList<ChatMessage> = mutableListOf();
    private var activityToRefresh: RefreshMsgs? = null

    public fun init(){
        try{
            SocketManager.initSocket()
        }catch (e: Exception){
            println("issue initializing chatservice: ")
            e.printStackTrace()
        }
        // init socket
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun refreshMessages(){
        // get messages from last date
        val dateStr = "2025-03-27 17:37:00.000"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val localDateTime = LocalDateTime.parse(dateStr, formatter)
        var msg = RefreshChats(SocketMessageTypes.REFRESH_MESSAGES,localDateTime)
        SocketManager.refreshMessages(msg)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    public fun addMessage(msg: ChatMessage){
        this.refreshActivity()
        messagesList.add(msg)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    public fun addMessages(msgs: List<ChatMessage>){
        messagesList.addAll(msgs)
        this.refreshActivity()
    }

    fun getMessagesByChatId(chatId: Int): List<ChatMessage> {
        return messagesList.filter { it.chatRoom.id == chatId }
    }

    fun updateActivityToRefresh(activity: RefreshMsgs?){
        this.activityToRefresh = activity;
    }

    fun refreshActivity(){
        if(this.activityToRefresh != null){
            this.activityToRefresh!!.runOnUiThread {
                this.activityToRefresh!!.refreshMessages()
            }
        }
    }
}