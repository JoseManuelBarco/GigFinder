package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivityFillAccountDetailsMusician : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity_fill_account_details_musician)

        val role = intent.getStringExtra("role")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val artisticNameEditText = findViewById<EditText>(R.id.artisticNameEditText)
        val hourlyFeeEditText = findViewById<EditText>(R.id.hourlyFeeEditText)
        val membersNumberEditText = findViewById<EditText>(R.id.membersNumberEditText)

        val musicalLanguageSpinner = findViewById<Spinner>(R.id.musicalLanguageSpinner)

        val musicalLanguages = listOf(
            "Inglés" to 1,
            "Español" to 2,
            "Francés" to 3
                                     )

        val languageNames = musicalLanguages.map { it.first }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageNames)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        musicalLanguageSpinner.adapter = adapter

        Log.d("RegisterActivityMap", "Rol: $role")
        Log.d("RegisterActivityMap", "Email: $email")
        Log.d("RegisterActivityMap", "Contraseña: $password")

        findViewById<TextView>(R.id.confirmButton).setOnClickListener {
            val artisticName = artisticNameEditText.text.toString()
            val hourlyFeeText = hourlyFeeEditText.text.toString()
            val membersNumberText = membersNumberEditText.text.toString()

            val selectedLanguageName = musicalLanguageSpinner.selectedItem.toString()

            // Obtener la ID del idioma musical seleccionada (la posición en el Spinner)
            val langId = musicalLanguages.find { it.first == selectedLanguageName }?.second ?: 0



            val hourlyFee = hourlyFeeText.toIntOrNull()
            val membersNumber = membersNumberText.toIntOrNull()

            Log.d("RegisterActivityMap", "Nombre artistico: $artisticName")
            Log.d("RegisterActivityMap", "Numero integrantes: $membersNumber")
            Log.d("RegisterActivityMap", "Tarifa horaria: $hourlyFee")
            Log.d("RegisterActivityMap", "Idioma musical: $selectedLanguageName")
            Log.d("RegisterActivityMap", "ID del idioma musical seleccionado: $langId")

            val nextIntent = Intent(this, RegisterActivitySelectGenre::class.java)
            nextIntent.putExtra("role", role)
            nextIntent.putExtra("email", email)
            nextIntent.putExtra("password", password)
            nextIntent.putExtra("artisticName", artisticName)
            nextIntent.putExtra("membersNumber", membersNumber)
            nextIntent.putExtra("hourlyFee", hourlyFee)
            nextIntent.putExtra("langId", langId)

            // Iniciar la siguiente actividad
            startActivity(nextIntent)
            finish()
        }
    }
}
