package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.objects.Local
import com.example.myapplication.objects.Musician
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

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
        // Creamos los datos según el rol
        val selectedGenres = if (!skipGenres) generosSeleccionados.map { genreMap[it] ?: 0 } else emptyList()

        val userData = when (role) {
            "Musician" -> {
                val artisticName = intent.getStringExtra("artisticName").toString()
                val membersNumber = intent.getIntExtra("membersNumber", 0)
                val hourlyFee = intent.getIntExtra("hourlyFee", 0) // Cambiado a int
                val langId = 1 // Esto debe ser el ID del lenguaje, puedes modificarlo si es necesario

                Musician(
                    idUser = 0, // Puede ser asignado por la base de datos
                    name = artisticName,
                    description = "Descripcion del músico",
                    email = email,
                    password = password,
                    rol = "Musician",  // O el rol que se necesita
                    avg_rating = 0,  // Default o asignar si es necesario
                    image_identifier = "", // Default o asignar si es necesario
                    size = membersNumber,
                    price = hourlyFee,
                    genres = selectedGenres,  // Lista de géneros seleccionados
                    langId = langId // Añadido langId
                        )
            }
            "Local" -> {
                val localName = intent.getStringExtra("localName").toString()
                val maximumCapacity = intent.getIntExtra("maximumCapacity", 0)
                val latitude = intent.getDoubleExtra("EXTRA_LATITUDE", 0.0)
                val longitude = intent.getDoubleExtra("EXTRA_LONGITUDE", 0.0)

                Local(
                    idUser = null,  // Puede ser asignado por la base de datos
                    name = localName,
                    description = "Descripcion del local",
                    email = email,
                    password = password,
                    rol = "Local", // O el rol que se necesita
                    avg_rating = 0,  // Default o asignar si es necesario
                    image_identifier = "",  // Default o asignar si es necesario
                    capacity = maximumCapacity,
                    x_coord = latitude,
                    y_coord = longitude
                     )
            }
            else -> null
        }

        // Enviar los datos a la API
        if (userData != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = when (role) {
                    "Musician" -> {
                        ApiClient.eventService.registerMusician(userData as Musician)
                    }
                    "Local" -> {
                        ApiClient.eventService.registerLocal(userData as Local)
                    }
                    else -> null
                }

                if (response?.isSuccessful == true) {
                    Toast.makeText(this@RegisterActivitySelectGenre, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Redirigir al siguiente paso o actividad
                } else {
                    val errorBody = response?.errorBody()?.string()
                    Log.e("RegisterActivity", "Error en el registro: $errorBody")
                    Toast.makeText(this@RegisterActivitySelectGenre, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}