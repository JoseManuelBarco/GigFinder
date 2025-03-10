package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class RegisterActivity6 : AppCompatActivity() {

    private val generosSeleccionados = mutableListOf<String>()
    private lateinit var textViewResumen: TextView
    private var nombre: String? = null
    private var correo: String? = null
    private var edad: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registeractivity4)

        // Obtener datos de la actividad anterior
        nombre = intent.getStringExtra("nombre")
        correo = intent.getStringExtra("correo")
        edad = intent.getStringExtra("edad")

        // Referencia al TextView donde se mostrará la información final
        textViewResumen = findViewById(R.id.textViewtitle)

        // Configurar selección de géneros
        configurarSeleccionGenero()

        // Configurar botones
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

        // Mostrar todos los datos del usuario
        val resumen = """
            Nombre: $nombre
            Correo: $correo
            Edad: $edad
            Géneros seleccionados: ${generosSeleccionados.joinToString(", ")}
        """.trimIndent()

        textViewResumen.text = resumen
    }

    private fun saltarSeleccion() {
        Toast.makeText(this, "Selección de género omitida", Toast.LENGTH_SHORT).show()
        finish()
    }
}