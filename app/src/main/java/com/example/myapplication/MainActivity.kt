// MainActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.objects.LoginRequest
import com.example.myapplication.objects.AuthResponse
import com.example.myapplication.objects.LoginResponse
import com.example.myapplication.objects.User
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity" // Etiqueta para los logs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val signupButton: ImageView = findViewById(R.id.loginButton)
        val loginTextView: TextView = findViewById(R.id.registerTextView)

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Realizar la autenticación con la API
            authenticateUser(email, password)
        }

        loginTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivityAccountType::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun authenticateUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        lifecycleScope.launch {
            try {
                Log.d(TAG, "Realizando solicitud de login con email: $email")

                val response: Response<String> = ApiClient.eventService.login(loginRequest)

                if (response.isSuccessful) {
                    val authResponse = response.body()

                    if (authResponse != null) {
                        Log.d(TAG, "Login exitoso. Respuesta del servidor: $authResponse")

                        // Llamamos directamente a getUserProfile usando el token recibido
                        getUserProfile(authResponse)

                    } else {
                        Log.e(TAG, "Error: No se recibió respuesta válida del servidor")
                        Toast.makeText(this@MainActivity, "Error al obtener la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "Error de autenticación: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@MainActivity, "Error de autenticación", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al conectar con el servidor: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun getUserProfile(token: String) {
        try {
            val authHeader = "Bearer $token"
            val response: Response<User> = ApiClient.eventService.getUserProfile(authHeader)

            if (response.isSuccessful) {
                val userProfile = response.body()

                if (userProfile != null) {
                    Log.d(TAG, "Perfil del usuario recibido: $userProfile")

                    if (userProfile.type == "music") {

                        val intent = Intent(this, MusicianOpportunitiesActivity::class.java)
                        startActivity(intent)

                    } else if (userProfile.type == "local") {

                        val intent = Intent(this, LocalOffersActivity::class.java)
                        startActivity(intent)

                    }

                } else {
                    Log.e(TAG, "No se recibió información de usuario válida")
                    Toast.makeText(this@MainActivity, "Error al obtener datos de usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e(TAG, "Error en la respuesta: ${response.code()} - ${response.message()}")
                Toast.makeText(this@MainActivity, "Error al obtener el perfil", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al conectar con el servidor: ${e.message}", e)
            Toast.makeText(this@MainActivity, "Error de red", Toast.LENGTH_SHORT).show()
        }
    }



}
