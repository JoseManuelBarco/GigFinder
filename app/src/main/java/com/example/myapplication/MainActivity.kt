package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val signupButton: ImageView = findViewById(R.id.loginButton)
        val loginTextView: TextView = findViewById(R.id.registerTextView)

        signupButton.setOnClickListener {
            val intent = Intent(this, RegisterActivityMap::class.java)
            startActivity(intent)
        }

        loginTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity1::class.java)
            startActivity(intent)
        }
    }
}