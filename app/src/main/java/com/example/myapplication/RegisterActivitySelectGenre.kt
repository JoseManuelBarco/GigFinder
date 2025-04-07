package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.objects.Local
import com.example.myapplication.objects.Musician
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

//import androidx.compose.foundation.content.MediaType

class RegisterActivitySelectGenre : AppCompatActivity() {

    private val generosSeleccionados = mutableListOf<String>()
    private lateinit var textViewResumen: TextView

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var role: String

    // Mapa de géneros a sus correspondientes IDs
    private val genreMap = mapOf(
        "Rock" to 1,
        "Electrónica" to 2,
        "Jazz" to 3,
        "Pop" to 4,
        "Hip-hop" to 5,
        "Clásica" to 6,
        "Reggaetón" to 7,
        "Soundtracks" to 8,
        "Flauta" to 9,
        "Otros" to 10
                                )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity_select_genre)

        textViewResumen = findViewById(R.id.textViewtitle)

        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        role = intent.getStringExtra("role").toString()
        configurarSeleccionGenero()
        setupButtonListeners()
    }

    private fun configurarSeleccionGenero() {
        val genreViews = listOf(
            R.id.rock to "Rock",
            R.id.electronica to "Electrónica",
            R.id.jazz to "Jazz",
            R.id.pop to "Pop",
            R.id.hiphop to "Hip-hop",
            R.id.clasica to "Clásica",
            R.id.latina_reggaeton to "Reggaetón",
            R.id.soundtracks to "Soundtracks",
            R.id.flauta to "Flauta",
            R.id.otros to "Otros"
                               )

        genreViews.forEach { (id, genreName) ->
            val frame = findViewById<FrameLayout>(id)
            val textView = (0 until frame.childCount)
                .map { frame.getChildAt(it) }
                .firstOrNull { it is TextView } as? TextView

            textView?.let {
                frame.setOnClickListener {
                    toggleGenreSelection(genreName, frame)
                }
            } ?: Log.e("RegisterActivity", "No se encontró TextView en el FrameLayout $id")
        }
    }

    private fun toggleGenreSelection(genreName: String, view: View) {
        if (generosSeleccionados.contains(genreName)) {
            generosSeleccionados.remove(genreName)
            view.alpha = 1.0f
        } else {
            generosSeleccionados.add(genreName)
            view.alpha = 0.5f
        }
    }

    private fun setupButtonListeners() {
        findViewById<View>(R.id.buttonConfirm).setOnClickListener {
            if (validateSelection()) {
                processRegistration()
            }
        }

        findViewById<View>(R.id.buttonSelectLater).setOnClickListener {
            processRegistration(skipGenres = true)
        }
    }

    private fun validateSelection(): Boolean {
        if (generosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un género", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun processRegistration(skipGenres: Boolean = false) {
        val genres = if (!skipGenres) {
            generosSeleccionados.map { genreMap[it] ?: 0 }.filter { it != 0 }
        } else {
            emptyList()
        }

        if (role == "Musician") {
            //post de musician

            var name = intent.getStringExtra("artisticName").toString()
            var size = intent.getIntExtra("membersNumber", 0)
            var price = intent.getIntExtra("hourlyFee", 0)
            var langId = intent.getIntExtra("langId", 0)

            val musician = Musician(
                email = email,
                name = name,
                description = "desc",
                password = password,
                size = size,
                price = price,
                langId = langId,
                genres = genres
                                   )

            val gson = Gson()
            val json = gson.toJson(musician)

            val requestBody = RequestBody.create("application/json".toMediaType(), json)
            // Forma NO-DEPRECATED: val requestBody = json.toRequestBody("application/json".toMediaType())

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ApiClient.eventService.registerMusician(requestBody)
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivitySelectGenre, "Músico creado exitosamente", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivitySelectGenre, "Error al crear el músico", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivitySelectGenre, "Error en la conexión", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("RegisterActivity", "Error al hacer la solicitud: ${e.message}")
                }
            }


        }

    }
}


