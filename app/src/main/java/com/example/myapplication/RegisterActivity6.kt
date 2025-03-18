package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.RegisterActivityFillAccountDetailsLocal.Companion.EXTRA_LATITUDE

class RegisterActivity6 : AppCompatActivity() {

    private val generosSeleccionados = mutableListOf<String>()
    private lateinit var textViewResumen: TextView
    private var nombre: String? = null
    private var correo: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registeractivity4)

        val nombreLocal = intent.getStringExtra("nombreLocal")
        val aforoMaximo = intent.getIntExtra("aforoMaximo", 0)
        val latitude = intent.getStringExtra(EXTRA_LATITUDE)
        val longitude = intent.getStringExtra(EXTRA_LATITUDE)



        Log.d("RegisterActivity6", "Nombre del local: $nombreLocal")
        Log.d("RegisterActivity6", "Aforo máximo: $aforoMaximo")
        Log.d("RegisterActivity6", "Latitud del marcador: $latitude")
        Log.d("RegisterActivity6", "Longitud del marcador: $longitude")


        nombre = intent.getStringExtra("nombre")
        correo = intent.getStringExtra("correo")

        textViewResumen = findViewById(R.id.textViewtitle)

        configurarSeleccionGenero()

        findViewById<View>(R.id.button).setOnClickListener { confirmarSeleccion() }
        findViewById<View>(R.id.button2).setOnClickListener { saltarSeleccion() }
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
        }
    }

    private fun saltarSeleccion() {
        Toast.makeText(this, "Selección de género omitida", Toast.LENGTH_SHORT).show()
        finish()
    }
}