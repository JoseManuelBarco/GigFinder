package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.chats.ChatActivity
import com.example.myapplication.chats.SocketManager
import com.example.myapplication.objects.User
import org.mindrot.jbcrypt.BCrypt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        SocketManager.initSocket()
        val activityChat = Intent(this, ChatActivity::class.java)
        activityChat.putExtra("chat_id", 1015)
        startActivity(activityChat)

        val signupButton: ImageView = findViewById(R.id.loginButton)
        val loginTextView: TextView = findViewById(R.id.registerTextView)

        signupButton.setOnClickListener {
            val intent = Intent(this, RegisterActivityFillAccountDetailsLocal::class.java)
            startActivity(intent)
        }

        loginTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivityAccountType::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun verifyUser(userName : TextView, password : TextView, userList : ArrayList<User>) : Boolean{
        var foundUser = false

        for (user in userList) {
            if (user.getEmail() == userName.text && BCrypt.checkpw(password.text.toString(), user.getPassword())) {
                foundUser = true
            }
        }

        return foundUser
    }
}