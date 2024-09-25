package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtNom: EditText = findViewById(R.id.TxtName)
        val btnOk: Button = findViewById(R.id.BtnOk)
        val lblNom: TextView = findViewById(R.id.LblNom)

        btnOk.setOnClickListener(){
            var text = txtNom.text.toString()
            if (text.equals("")){
                lblNom.text = "El teu nom és " + text
            }
            else
            {
                lblNom.text = ""
                Toast.makeText(this, "No has introduit el teu nom",Toast.LENGTH_LONG).show()
            }
        }
    }
}