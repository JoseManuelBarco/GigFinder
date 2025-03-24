package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivityFillAccountDetailsMusician  : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity3musician)


        val role = intent.getStringExtra("role")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val artisticNameEditText = findViewById<EditText>(R.id.artisticNameEditText)
        val hourlyFeeEditText = findViewById<EditText>(R.id.hourlyFeeEditText)
        val membersNumberEditText = findViewById<EditText>(R.id.membersNumberEditText)

        val musicalLanguageSpinner = findViewById<Spinner>(R.id.musicalLanguageSpinner)

        // Obtener el array de valores de "musical_languages" definido en strings.xml
        val musicalLanguages = resources.getStringArray(R.array.musical_languages)

        // Crear un ArrayAdapter con el array de valores
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, musicalLanguages)

        // Definir el layout para la vista desplegable del Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        // Asignar el adapter al Spinner
        musicalLanguageSpinner.adapter = adapter

        // Mostrar datos en Logcat para depuración
        Log.d("RegisterActivityMap", "Rol: $role")
        Log.d("RegisterActivityMap", "Email: $email")
        Log.d("RegisterActivityMap", "Contraseña: $password")

        // Al hacer click en el botón, recolectar la información y pasarla a la siguiente actividad
        findViewById<TextView>(R.id.confirmButton).setOnClickListener {
            val artisticName = artisticNameEditText.text.toString()
            val hourlyFeeText = hourlyFeeEditText.text.toString()
            val membersNumberText = membersNumberEditText.text.toString()
            val musicalLanguage = musicalLanguageSpinner.selectedItem.toString()

            val hourlyFee = hourlyFeeText.toDoubleOrNull()
            val membersNumber = membersNumberText.toIntOrNull()


            Log.d("RegisterActivityMap", "Nombre artistico: $artisticName")
            Log.d("RegisterActivityMap", "Numero integrantes: $membersNumber")
            Log.d("RegisterActivityMap", "Tarifa horaria: $hourlyFee")
            Log.d("RegisterActivityMap", "Idioma musical: $musicalLanguage")


            val nextIntent = Intent(this, RegisterActivity6::class.java)
            nextIntent.putExtra("role", role) // Pasar el rol
            nextIntent.putExtra("email", email) // Pasar el email
            nextIntent.putExtra("password", password)
            nextIntent.putExtra("artisticName", artisticName)
            nextIntent.putExtra("membersNumber", membersNumber)
            nextIntent.putExtra("hourlyFee", hourlyFee)
            nextIntent.putExtra("musicalLanguage", musicalLanguage)



            startActivity(nextIntent)
                finish()
        }
    }

}
