package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivityAccountType : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity_account_type)

        val imgSelectLocal = findViewById<ImageView>(R.id.imgSelectLocal)
        val imgSelectMusician = findViewById<ImageView>(R.id.imgSelectMusician)
        val signupButton = findViewById<ImageView>(R.id.signupButton)

        var selectedRole: String? = null

        imgSelectLocal.setOnClickListener {
            selectedRole = "Local"
            imgSelectLocal.setBackgroundResource(R.drawable.selectlocalviolet)
            imgSelectMusician.setBackgroundResource(R.drawable.selectmusicianwhite)
        }

        imgSelectMusician.setOnClickListener {
            selectedRole = "Musician"
            imgSelectMusician.setBackgroundResource(R.drawable.selectmusicianviolet)
            imgSelectLocal.setBackgroundResource(R.drawable.selectlocalwhite)
        }

        signupButton.setOnClickListener {
            if (selectedRole == null) {
                Toast.makeText(this, "Por favor, selecciona un rol", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, RegisterActivityPersonalData::class.java)
            intent.putExtra("role", selectedRole)
            Log.d("RegisterActivityAccountType", "Role: $selectedRole")
            startActivity(intent)
        }


    }
}