package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class RegisterActivitySelectGenre : AppCompatActivity() {

    private val generosSeleccionados = mutableListOf<String>()
    private lateinit var textViewResumen: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity_select_genre)



        val role = intent.getStringExtra("role")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        Log.d("RegisterActivity6", "Role: $role")




        if (role.equals("Musician")) {

            val artisticName = intent.getStringExtra("artisticName")

            val membersNumber = intent.getIntExtra("membersNumber", 0)

            val hourlyFee = intent.getDoubleExtra("hourlyFee", 0.0)

            val langId = intent.getIntExtra("langId", -1) // -1 es el valor por defecto en caso de que no se pase el dato


            Log.d("RegisterActivity6", "Nombre artistico: $artisticName")
            Log.d("RegisterActivity6", "Numero integrantes: $membersNumber")
            Log.d("RegisterActivity6", "Tarifa horaria: $hourlyFee")
            Log.d("RegisterActivityMap", "Idioma musical: $langId")



        } else if (role.equals("Local")){

            val localName = intent.getStringExtra("nombreLocal")
            val maximumCapacity = intent.getIntExtra("maximumCapacity", 0)
            val latitude = intent.getDoubleExtra("EXTRA_LATITUDE", 0.0)
            val longitude = intent.getDoubleExtra("EXTRA_LONGITUDE", 0.0)

            Log.d("RegisterActivity6", "Nombre del local: $localName")
            Log.d("RegisterActivity6", "Aforo máximo: $maximumCapacity")
            Log.d("RegisterActivity6", "Latitud del marcador: $latitude")
            Log.d("RegisterActivity6", "Longitud del marcador: $longitude")
            Log.d("RegisterActivity6", "Email: $email")
            Log.d("RegisterActivity6", "Password: $password")

        }

        textViewResumen = findViewById(R.id.textViewtitle)

        configurarSeleccionGenero()

        findViewById<View>(R.id.buttonConfirm).setOnClickListener {

            confirmarSeleccion()

            //crear objeto de usuario

            var intent = Intent(this, MusicianOpportunitiesActivity::class.java)

            startActivity(intent)



        }
        findViewById<View>(R.id.buttonSelectLater).setOnClickListener {


            saltarSeleccion()


        }
    }

    private fun configurarSeleccionGenero() {
        val ids = listOf(
            R.id.rock, R.id.electronica, R.id.jazz, R.id.pop,
            R.id.hiphop, R.id.clasica, R.id.latina_reggaeton, R.id.soundtracks,
            R.id.flauta, R.id.otros
                        )

        for (id in ids) {
            val frame = findViewById<FrameLayout>(id)
            frame.setOnClickListener {
                val genero = (frame.getChildAt(1) as TextView).text.toString()
                if (generosSeleccionados.contains(genero)) {
                    generosSeleccionados.remove(genero)
                    frame.alpha = 1.0f
                } else {
                    generosSeleccionados.add(genero)
                    frame.alpha = 0.5f
                }
            }
        }
    }



    private fun confirmarSeleccion() {
        if (generosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un género", Toast.LENGTH_SHORT).show()
            return
        } else {
            val role = intent.getStringExtra("role")
            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")

            val jsonData = if (role == "Musician") {
                // JSON para el músico
                val artisticName = intent.getStringExtra("artisticName")
                val membersNumber = intent.getIntExtra("membersNumber", 0)
                val hourlyFee = intent.getDoubleExtra("hourlyFee", 0.0)
                val langId = intent.getIntExtra("langId", -1)

                val genres = generosSeleccionados.map { genero ->
                    // Aquí asumes que tienes un mapeo de géneros a IDs.
                    when (genero) {
                        "Rock" -> 1
                        "Electrónica" -> 2
                        "Jazz" -> 3
                        "Pop" -> 4
                        "Hip-hop" -> 5
                        "Clásica" -> 6
                        "Reggaetón" -> 7
                        "Soundtracks" -> 8
                        "Flauta" -> 9
                        "Otros" -> 10
                        else -> 0
                    }
                }

                val musicianJson = JSONObject()
                musicianJson.put("email", email)
                musicianJson.put("name", artisticName)
                musicianJson.put("description", JSONObject.NULL) // O la descripción que se proporcione
                musicianJson.put("password", password)
                musicianJson.put("size", membersNumber)
                musicianJson.put("price", hourlyFee)
                musicianJson.put("langId", langId)
                musicianJson.put("genres", JSONArray(genres))

                musicianJson.toString()

            } else {
                // JSON para el local
                val nombreLocal = intent.getStringExtra("nombreLocal")
                val aforoMaximo = intent.getIntExtra("aforoMaximo", 0)
                val latitude = intent.getDoubleExtra("EXTRA_LATITUDE", 0.0)
                val longitude = intent.getDoubleExtra("EXTRA_LONGITUDE", 0.0)

                val localJson = JSONObject()
                localJson.put("email", email)
                localJson.put("name", nombreLocal)
                localJson.put("description", JSONObject.NULL) // O la descripción que se proporcione
                localJson.put("password", password)
                localJson.put("capacity", aforoMaximo)
                localJson.put("x_coordination", latitude)
                localJson.put("y_coordination", longitude)

                localJson.toString()
            }

            Log.d("JSON Data", jsonData)
            // Aquí enviarías el jsonData a un servidor o lo guardarías como necesites.
            // Ejemplo: enviar a un servidor, o guardar en preferencias compartidas.
        }
    }
    private fun saltarSeleccion() {
        val role = intent.getStringExtra("role")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val jsonData = if (role == "Musician") {
            // JSON para el músico con géneros como null
            val artisticName = intent.getStringExtra("artisticName")
            val membersNumber = intent.getIntExtra("membersNumber", 0)
            val hourlyFee = intent.getDoubleExtra("hourlyFee", 0.0)
            val langId = intent.getIntExtra("langId", -1)

            val musicianJson = JSONObject()
            musicianJson.put("email", email)
            musicianJson.put("name", artisticName)
            musicianJson.put("description", JSONObject.NULL)
            musicianJson.put("password", password)
            musicianJson.put("size", membersNumber)
            musicianJson.put("price", hourlyFee)
            musicianJson.put("langId", langId)
            musicianJson.put("genres", JSONObject.NULL) // Géneros omitidos

            musicianJson.toString()

        } else {
            // JSON para el local
            val nombreLocal = intent.getStringExtra("nombreLocal")
            val aforoMaximo = intent.getIntExtra("aforoMaximo", 0)
            val latitude = intent.getDoubleExtra("EXTRA_LATITUDE", 0.0)
            val longitude = intent.getDoubleExtra("EXTRA_LONGITUDE", 0.0)

            val localJson = JSONObject()
            localJson.put("email", email)
            localJson.put("name", nombreLocal)
            localJson.put("description", JSONObject.NULL)
            localJson.put("password", password)
            localJson.put("capacity", aforoMaximo)
            localJson.put("x_coordination", latitude)
            localJson.put("y_coordination", longitude)

            localJson.toString()
        }

    }
}