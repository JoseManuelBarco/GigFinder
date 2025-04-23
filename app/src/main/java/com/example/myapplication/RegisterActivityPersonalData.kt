package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.objects.User

class RegisterActivityPersonalData : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmpasswordEditText)
        val continueButton = findViewById<TextView>(R.id.continueButton)
        val role = intent.getStringExtra("role")

        Log.d("RegisterActivityPersonalData", "Role: $role")


        continueButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()



            if ( email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intentPersonalData = when (role) {
                "Musician" -> Intent(this, RegisterActivityFillAccountDetailsMusician::class.java) // Si es músico
                "Local" -> Intent(this, RegisterActivityFillAccountDetailsLocal::class.java) // Si es local
                else -> throw IllegalArgumentException("Rol desconocido") // Si el rol no es ni "Musician" ni "Local"
            }
            intentPersonalData.putExtra("role", role)
            intentPersonalData.putExtra("email", email)
            intentPersonalData.putExtra("password", password)
            startActivity(intentPersonalData)
            finish()
        }
    }
}