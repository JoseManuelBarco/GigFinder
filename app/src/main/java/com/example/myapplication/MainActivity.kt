package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.chats.ChatActivity
import com.example.myapplication.chats.ChatListActivity
import com.example.myapplication.chats.ChatService
import com.example.myapplication.chats.SocketManager
import com.example.myapplication.objects.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        ChatService.init()
//        val activityChat = Intent(this, ChatActivity::class.java)
//        activityChat.putExtra("chat_id", 1015)
//        startActivity(activityChat)
//        val activityChatList = Intent(this, ChatListActivity::class.java)
//        startActivity(activityChatList)

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
        CoroutineScope(Dispatchers.Main).launch {
            val loginData = mapOf("email" to email, "password" to password)

            try {
                // Llamar a la API para autenticar al usuario
                val response: Response<Map<String, Any>> = ApiClient.eventService.login(loginData)

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    // Aquí puedes obtener los detalles del usuario de la respuesta
                    val userRole = responseBody?.get("role") as? String
                    val userEmail = responseBody?.get("email") as? String
                    val userName = responseBody?.get("name") as? String

                    if (userRole == "Musician") {
                        // Si es un músico, ir a MusicianOpportunitiesActivity
                        val intent = Intent(this@MainActivity, MusicianOpportunitiesActivity::class.java)
                        intent.putExtra("email", userEmail)
                        intent.putExtra("name", userName)
                        startActivity(intent)
                    } else if (userRole == "Local") {
                        // Si es un local, no hacer nada o mostrar mensaje de error
                        Toast.makeText(this@MainActivity, "Es un Local, no un Músico", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si la autenticación falla, mostrar mensaje de error
                    Toast.makeText(this@MainActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}