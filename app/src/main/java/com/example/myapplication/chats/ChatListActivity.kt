package com.example.myapplication.chats

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.chats.data.ChatRoom

class ChatListActivity : AppCompatActivity(), RefreshMsgs {
    var chats: List<ChatRoom> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // set self as activity to refresh
        ChatService.updateActivityToRefresh(this)

        // get chats
        chats = ChatService.getChatrooms()

        val userId = 1

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChatRoomAdapter(chats, userId) { chatId, userName ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chat_id", chatId)
            intent.putExtra("user_name", userName)
            startActivity(intent)
        }

    }

    override fun refreshMessages() {
        chats = ChatService.getChatrooms()
    }

    override fun runOnUiThread(function: () -> Unit) {
        super.runOnUiThread(function) // Use Activity's method
    }
}