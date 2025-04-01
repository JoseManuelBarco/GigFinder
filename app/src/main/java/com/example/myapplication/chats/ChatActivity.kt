package com.example.myapplication.chats

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.constants.SocketMessageTypes
import com.example.myapplication.chats.data.ChatMessage
import com.example.myapplication.chats.data.SendChatMessageBody

class ChatActivity : AppCompatActivity(), RefreshMsgs {

    private var chatId: Int = 0;
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var messagesList: List<ChatMessage> = mutableListOf()

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

        // initialize variables
        recyclerView = findViewById(R.id.messagesList)
        chatId = intent.getIntExtra("chat_id", 0);
        val username = intent.getStringExtra("user_name")

        // set this activity at chat service as activity to refresh
        ChatService.updateActivityToRefresh(this)

        // get initial messages
        this.refreshMessages()
        // Add user name
        val userNameElement: TextView = findViewById(R.id.username)
        userNameElement.text = username
        // Add back click listener
        val backClicListener: ImageView = findViewById((R.id.backButton))
        backClicListener.setOnClickListener {
            val activityChatList = Intent(this, ChatListActivity::class.java)
            startActivity(activityChatList)
        }
        // Set send msg click listener
        val sendMsgButton: ImageView = findViewById(R.id.sendMsg)
        sendMsgButton.setOnClickListener {
            val messageInput: EditText = findViewById(R.id.messageInput)
            val message = messageInput.text.toString()

            if (message.length > 0) {
                // clear input
                messageInput.text.clear()

                // print
                println(message)

                // create msg and send it
                var msg = SendChatMessageBody(SocketMessageTypes.SEND_MESSAGE, chatId, message,)
                SocketManager.sendMessage(msg)
            }
        }
    }

    override fun runOnUiThread(function: () -> Unit) {
        super.runOnUiThread(function) // Use Activity's method
    }

    fun renderMessages(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messagesList, 1)
        recyclerView.adapter = chatAdapter
        recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun refreshMessages() {
        messagesList = ChatService.getMessagesByChatId(this.chatId)
        renderMessages()
    }
}