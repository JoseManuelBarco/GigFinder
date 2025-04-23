package com.example.myapplication

import LocalEventsAdapter
import android.app.TimePickerDialog
import com.jakewharton.threetenabp.AndroidThreeTen
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.api_objects.EventPostBody
import com.example.myapplication.helpers.PreferencesHelper
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.util.Calendar

class LocalOffersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocalEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_local)

        val prefs = PreferencesHelper(this)
        val user = prefs.getUserProfile()

        if (user != null) {
            // Puedes usar user.name, user.type, etc.
            Log.d("OtherActivity", "Nombre del usuario: ${user.name}")
        } else {
            Log.e("OtherActivity", "No se encontró perfil de usuario")
        }


        recyclerView = findViewById(R.id.recyclerViewLocalList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LocalEventsAdapter(listOf())
        recyclerView.adapter = adapter

        loadMyEvents()

        val createOfferButton = findViewById<TextView>(R.id.textViewCreateOfferButton)
        val createOfferFrame = findViewById<FrameLayout>(R.id.createOfferFrame)




        createOfferButton.setOnClickListener {
            if (createOfferFrame.childCount == 0) {
                val offerFormView = LayoutInflater.from(this).inflate(R.layout.create_offer_layout, createOfferFrame, false)
                createOfferFrame.addView(offerFormView)
                createOfferFrame.visibility = View.VISIBLE


                val musicalGenreSpinner = findViewById<Spinner>(R.id.selectGenreSpinner)

                val timeStartEditText = offerFormView.findViewById<EditText>(R.id.timeStartEditText)
                val timeImageView = offerFormView.findViewById<ImageView>(R.id.eventTimeImageView)

                val dateStartEditText = offerFormView.findViewById<EditText>(R.id.dateStartEditText)
                val dateImageView = offerFormView.findViewById<ImageView>(R.id.eventDateImageView)

                val musicalGenre = listOf(
                    "Cualquiera" to 0,
                    "Rock" to 1,
                    "Jazz" to 2,
                    "Clásica" to 3,
                    "Pop" to 4,
                    "HipHop" to 5,
                    "Latina/Reggaeton" to 6,
                    "Electronica" to 7,
                    "Soundtrack" to 8,
                    "Flauta" to 9,
                    "Others" to 10
                                         )

                val genreNames = musicalGenre.map { it.first }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genreNames)

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                musicalGenreSpinner.adapter = adapter

                timeImageView.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    TimePickerDialog(
                        this@LocalOffersActivity,
                        { _, hour, minute ->
                            timeStartEditText.setText(String.format("%02d:%02d", hour, minute))
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                                    ).show()
                }

                timeStartEditText.isClickable = false
                timeStartEditText.isFocusable = false

                dateImageView.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    val datePickerDialog = android.app.DatePickerDialog(
                        this@LocalOffersActivity,
                        { _, selectedYear, selectedMonth, selectedDay ->
                            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                            dateStartEditText.setText(formattedDate)
                        },
                        year, month, day
                                                                       )
                    datePickerDialog.show()
                }

                dateStartEditText.isClickable = false
                dateStartEditText.isFocusable = false




                // Opcional: agregar botón de cerrar en tu layout inflado
                val saveButton = offerFormView.findViewById<TextView>(R.id.createSaveButtonTextView) // O el que uses para "Crear/Guardar"
                saveButton.setOnClickListener {
                    val dateStart = dateStartEditText.text.toString() // "2025-03-10"
                    val startTime = timeStartEditText.text.toString() // "10:00"
                    val duration = offerFormView.findViewById<EditText>(R.id.durationEditText).text.toString().toIntOrNull() ?: 0
                    val price = offerFormView.findViewById<EditText>(R.id.paymentEditText).text.toString().toIntOrNull() ?: 0
                    val description = offerFormView.findViewById<EditText>(R.id.descriptionEditText).text.toString()
                    val genreId = musicalGenreSpinner.selectedItemPosition

                    if (duration <= 0) {
                        Toast.makeText(this, "Duración debe ser un número positivo", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (price <= 0) {
                        Toast.makeText(this, "El pago debe ser un número positivo", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val (dateStartFormatted, dateEndFormatted) = calcularFechaFinal(dateStart, startTime, duration)

                    val eventPostBody = EventPostBody(
                        dateStart = dateStartFormatted,
                        dateEnd = dateEndFormatted,
                        price = price,
                        description = description,
                        genreId = genreId
                                                     )

                    lifecycleScope.launch {
                        try {
                            val response = ApiClient.eventService.createEvent(eventPostBody)
                            if (response.isSuccessful) {
                                Log.d("CreateEvent", "Evento creado correctamente")
                                Toast.makeText(this@LocalOffersActivity, "Evento creado", Toast.LENGTH_SHORT).show()
                                createOfferFrame.removeAllViews()
                                createOfferFrame.visibility = View.GONE
                                loadMyEvents()
                            } else {
                                Log.e("CreateEvent", "Error en la creación del evento: ${response.code()}")
                                Toast.makeText(this@LocalOffersActivity, "Error al crear evento", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("CreateEvent", "Excepción: ${e.message}")
                            Toast.makeText(this@LocalOffersActivity, "Fallo de red o del servidor", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            createOfferFrame.visibility = View.VISIBLE
        }

        }

    fun calcularFechaFinal(dateStr: String, timeStr: String, duracionMin: Int): Pair<String, String> {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTimeInicio = LocalDateTime.parse("${dateStr}T$timeStr", inputFormatter)
        val dateTimeFin = dateTimeInicio.plusMinutes(duracionMin.toLong())
        return Pair(
            dateTimeInicio.format(outputFormatter),
            dateTimeFin.format(outputFormatter) )
    }

    private fun loadMyEvents() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.eventService.getMyEvents()
                if (response.isSuccessful) {
                    val events = response.body() ?: emptyList()
                    adapter.updateData(events)
                } else {
                    Log.e("LoadEvents", "Error response: ${response.errorBody()?.string()}")
                    Toast.makeText(this@LocalOffersActivity, "No se pudieron cargar los eventos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LoadEvents", "Excepción: ${e.message}")
                Toast.makeText(this@LocalOffersActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }



    }