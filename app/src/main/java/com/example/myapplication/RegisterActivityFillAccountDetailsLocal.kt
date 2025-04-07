package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.events.MapEventsReceiver
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.objects.Local
import com.example.myapplication.objects.User
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class RegisterActivityFillAccountDetailsLocal : AppCompatActivity() {
    private lateinit var osmMapView: MapView
    private var singleMarker: Marker? = null

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        //const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
       //const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
    }

    private val TAG = "MusicianOpportunities"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity_fill_account_details_local)



        Configuration.getInstance().userAgentValue = packageName
        osmMapView = findViewById(R.id.osmMapView)
        osmMapView.setMultiTouchControls(true)

        checkLocationPermission() // Verificar permisos de ubicación

        setupMapTouchOverlay() // Configurar la superposición de eventos en el mapa


        val role = intent.getStringExtra("role")
        val email = intent.getStringExtra("email").toString()
        val password = intent.getStringExtra("password").toString()

        val localNameEditText = findViewById<EditText>(R.id.localNameEditText)
        val maximumCapacityEditText = findViewById<EditText>(R.id.maximumCapacityEditText)

        // Mostrar datos en Logcat para depuración
        Log.d("RegisterActivityLocalMap", "Rol: $role")
        Log.d("RegisterActivityLocalMap", "Email: $email")
        Log.d("RegisterActivityLocalMap", "Contraseña: $password")

        // Al hacer click en el botón, recolectar la información y pasarla a la siguiente actividad
        findViewById<ImageView>(R.id.confirmButton).setOnClickListener {
            val maximumCapacityText = maximumCapacityEditText.text.toString()
            val capacity = maximumCapacityText.toInt()
            val name = localNameEditText.text.toString()


            singleMarker?.let {
                val latitude = it.position.latitude
                val longitude = it.position.longitude
                Log.d("RegisterActivityLocalMap", "Latitud del marcador: $latitude")
                Log.d("RegisterActivityLocalMap", "Longitud del marcador: $longitude")


                var x_coordination = latitude;
                var y_coordination = longitude;

                val local = Local(
                    email = email,
                    name = name,
                    description = "desc",  // Puedes capturar la descripción si lo deseas
                    password = password,
                    capacity = capacity,
                    x_coordination = x_coordination,
                    y_coordination = y_coordination
                                 )

                val gson = Gson()
                val json = gson.toJson(local)


                val requestBody = json.toRequestBody("application/json".toMediaType())

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiClient.eventService.registerLocal(requestBody)
                        if (response.isSuccessful) {
                            runOnUiThread {
                                Toast.makeText(this@RegisterActivityFillAccountDetailsLocal, "Local creado exitosamente", Toast.LENGTH_SHORT).show()

                                val nextIntent = Intent(this@RegisterActivityFillAccountDetailsLocal, LocalOffersActivity::class.java)
                                startActivity(nextIntent)
                                finish()

                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@RegisterActivityFillAccountDetailsLocal, "Error al crear el local", Toast.LENGTH_SHORT).show()
                            }
                            Log.e("RegisterActivity", "Error al crear el local. Código: ${response.code()}, Cuerpo: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivityFillAccountDetailsLocal, "Error en la conexión", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("RegisterActivity", "Error al hacer la solicitud: ${e.message}")
                    }
                }


                // Crear un intent para pasar todos los datos a la siguiente actividad

            } ?: run {
                Log.d("RegisterActivityMap", "No se ha seleccionado un marcador.")
            }
            //AQUI SUBIR A LA BASE DE DATOS EL USUARIO
        }
    }



    private fun setupMapTouchOverlay() {
        osmMapView.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    if (singleMarker == null) {
                        singleMarker = Marker(osmMapView).apply {
                            position = it
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }
                        osmMapView.overlays.add(singleMarker)
                    } else {
                        singleMarker?.position = it
                    }
                    osmMapView.invalidate()
                    Log.d("RegisterActivityMap", "Marcador colocado en: ${it.latitude}, ${it.longitude}")
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean = false
        }))
    }

    // Método para verificar los permisos de ubicación
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            setUserLocation() // Si ya tiene permisos, establecer la ubicación del usuario
        }
    }

    private fun setUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnown?.let {
                val userLocation = GeoPoint(it.latitude, it.longitude)
                osmMapView.controller.setCenter(userLocation)
                osmMapView.controller.setZoom(15.0)

                Log.d("RegisterActivityMap", "Ubicación del usuario: ${it.latitude}, ${it.longitude}")
            } ?: run {
                // Si no se tiene ubicación conocida, centrar el mapa en Barcelona
                centerMapOnBarcelona()
            }
        } else {
            // Si no se tienen permisos de ubicación, centrar el mapa en Barcelona
            centerMapOnBarcelona()
        }
    }

    private fun centerMapOnBarcelona() {
        val barcelona = GeoPoint(41.3851, 2.1734)  // Coordenadas más precisas del centro de Barcelona
        try {
            // Configura el zoom primero
            osmMapView.controller.setZoom(15.0)

            // Luego establece el centro
            //osmMapView.controller.animateTo(barcelona)

            // Alternativa más directa:
             osmMapView.controller.setCenter(barcelona)
            // osmMapView.controller.setZoom(15.0)

            Log.d(TAG, "Mapa centrado en Barcelona: ${barcelona.latitude}, ${barcelona.longitude}")
        } catch (e: Exception) {
            Log.e(TAG, "Error al centrar mapa: ${e.message}")
        }
    }

    // Manejo de permisos de ubicación
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setUserLocation() // Establecer ubicación si el permiso es concedido
        } else {
            centerMapOnBarcelona() // Si el permiso no es concedido, centrar en Barcelona
        }
    }

    override fun onResume() {
        super.onResume()
        osmMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        osmMapView.onPause()
    }
}
