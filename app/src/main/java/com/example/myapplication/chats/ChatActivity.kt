package com.example.myapplication.chats

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.constants.SocketMessageTypes
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

        val chat_id = intent.getIntExtra("chat_id", 0);

        // Get button by ID
        val sendMsgButton: ImageView = findViewById(R.id.sendMsg)

        // Set click listener
        sendMsgButton.setOnClickListener {
            val messageInput: EditText = findViewById(R.id.messageInput)
            val message = messageInput.text.toString()

            if (message.length > 0) {
                // clear input
                messageInput.text.clear()

                // print
                println(message)

                // create msg and send it
                var msg = ChatMessage(SocketMessageTypes.SEND_MESSAGE,chat_id, message,)
                SocketManager.sendMessage(msg)
            }

//            var msg = ChatMessage("send-message",1015, "test msg",)
//            SocketManager.sendMessage(msg)
//
//            val dateStr = "2025-03-27 17:37:00.000"
//
//            // Define the DateTimeFormatter with the pattern that matches the string format
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//
//            // Parse the string to LocalDateTime
//            val localDateTime = LocalDateTime.parse(dateStr, formatter)
//
//            var msg = RefreshChats("refresh-messages",localDateTime)
//
//            SocketManager.refreshMessages(msg)

        }
    }
}