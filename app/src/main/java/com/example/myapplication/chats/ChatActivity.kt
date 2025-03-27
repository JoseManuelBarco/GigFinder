package com.example.myapplication.chats

import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.data.ChatMessage
import com.example.myapplication.data.RefreshChats
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ChatActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get button by ID
        val sendMsgButton = findViewById<Button>(R.id.sendMsg)

        // Set click listener
        sendMsgButton.setOnClickListener {
            println("click")

//            var msg = ChatMessage("send-message",1015, "test msg",)
//            SocketManager.sendMessage(msg)

            val dateStr = "2025-03-27 17:37:00.000"

            // Define the DateTimeFormatter with the pattern that matches the string format
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

            // Parse the string to LocalDateTime
            val localDateTime = LocalDateTime.parse(dateStr, formatter)

            var msg = RefreshChats("refresh-messages",localDateTime)

            SocketManager.refreshMessages(msg)
        }
    }
}