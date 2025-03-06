package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.objects.RegisterUser

class RegisterActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        val imgSelectLocal = findViewById<ImageView>(R.id.imgSelectLocal)
        val imgSelectMusician = findViewById<ImageView>(R.id.imgSelectMusician)
        val signupButton = findViewById<ImageView>(R.id.signupButton)

        var selectedRole: String? = null

        imgSelectLocal.setOnClickListener {
            selectedRole = "Local"
            imgSelectLocal.setBackgroundResource(R.drawable.selectmusicianviolet) // Indicar selección
            imgSelectMusician.setBackgroundResource(R.drawable.selectlocalwhite)
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

            val intent = Intent(this, RegisterActivity2::class.java)
            intent.putExtra("rol", selectedRole)
            startActivity(intent)
        }
    }
}