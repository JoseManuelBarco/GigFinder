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

        // Inicialización de vistas
        textViewResumen = findViewById(R.id.textViewtitle)

        // Obtención de datos básicos
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        role = intent.getStringExtra("role").toString()

        // Configuración común
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
                // Crear al músico o local con los géneros musicales seleccionados
                processRegistration()
            }
        }

        findViewById<View>(R.id.buttonSelectLater).setOnClickListener {
            // Registrar al músico o local sin géneros musicales seleccionados
            processRegistration(skipGenres = true)
        }
    }

    private fun validateSelection(): Boolean {
        // Verificar que se haya seleccionado al menos un género
        if (generosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un género", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun processRegistration(skipGenres: Boolean = false) {
        val selectedGenres = if (!skipGenres) generosSeleccionados.map { genreMap[it] ?: 0 } else emptyList()

        val gson = Gson()
        val jsonBody = when (role) {
            "Musician" -> {
                val artisticName = intent.getStringExtra("artisticName").toString()
                val membersNumber = intent.getIntExtra("membersNumber", 0)
                val hourlyFee = intent.getIntExtra("hourlyFee", 0)
                val langId = 1 // Asumimos que el langId es 1

                val musician = Musician(
                    email = email,
                    name = artisticName,
                    description = "Descripcion del músico", // Descripción estática, puedes cambiarla según sea necesario
                    password = password,
                    size = membersNumber,
                    price = hourlyFee,
                    langId = langId,
                    genres = selectedGenres
                                       )

                // Convertimos el objeto 'musician' a JSON
                val json = gson.toJson(musician)

                // Log para verificar que el JSON se genera correctamente
                Log.d("RegisterActivity", "JSON Body for Musician: $json")
                json
            }

            "Local" -> {
                val localName = intent.getStringExtra("localName").toString()
                val maximumCapacity = intent.getIntExtra("maximumCapacity", 0)
                val latitude = intent.getDoubleExtra("EXTRA_LATITUDE", 0.0)
                val longitude = intent.getDoubleExtra("EXTRA_LONGITUDE", 0.0)

                val local = Local(
                    name = localName,
                    description = "Descripcion del local", // Descripción estática, puedes cambiarla según sea necesario
                    email = email,
                    password = password,
                    capacity = maximumCapacity,
                    x_coord = latitude,
                    y_coord = longitude
                                 )

                // Convertimos el objeto 'local' a JSON
                val json = gson.toJson(local)

                // Log para verificar que el JSON se genera correctamente
                Log.d("RegisterActivity", "JSON Body for Local: $json")
                json
            }
            else -> null
        }

        // Verificamos si jsonBody no es nulo y llamamos a la función de registro con el JSON
        jsonBody?.let {/*
            registerMusicianWithJson(it) { success, message ->
                if (success) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Redirigir a otra actividad si es necesario
                } else {
                    Toast.makeText(this, "Error en el registro: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }*/
    }}}


    // Función para registrar al músico
// Función modificada para registrar al músico enviando el JSON
    /*private fun registerMusicianWithJson(jsonBody: String, onResult: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Convertimos el JSON a RequestBody correctamente
                val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody)

                // Llamamos al servicio de Retrofit con el JSON
                val response: Response<Map<String, Any>> = ApiClient.eventService.registerMusicianWithJson(body)

                if (response.isSuccessful) {
                    val result = response.body()
                    onResult(true, result?.get("message")?.toString() ?: "Registro exitoso")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error en el registro"
                    Log.e("RegisterMusician", "Error en el registro: $errorMessage")
                    onResult(false, errorMessage)
                }
            } catch (e: Exception) {
                Log.e("RegisterMusician", "Error: ${e.localizedMessage}", e)
                onResult(false, "Error: ${e.localizedMessage}")
            }
        }
    }*/


//}
