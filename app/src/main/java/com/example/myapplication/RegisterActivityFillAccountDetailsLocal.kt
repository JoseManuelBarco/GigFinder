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
import com.example.myapplication.objects.Local
import com.example.myapplication.objects.User

class RegisterActivityFillAccountDetailsLocal : AppCompatActivity() {
    private lateinit var osmMapView: MapView
    private var singleMarker: Marker? = null

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        //const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
       //const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity_fill_account_details_local)


        Configuration.getInstance().userAgentValue = packageName
        osmMapView = findViewById(R.id.osmMapView)
        osmMapView.setMultiTouchControls(true)

        checkLocationPermission() // Verificar permisos de ubicación

        setupMapTouchOverlay() // Configurar la superposición de eventos en el mapa

        var xcoord = 0.0

        var ycoord = 0.0

        val role = intent.getStringExtra("rol")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val localNameEditText = findViewById<EditText>(R.id.localNameEditText)
        val maximumCapacityEditText = findViewById<EditText>(R.id.maximumCapacityEditText)

        // Mostrar datos en Logcat para depuración
        Log.d("RegisterActivityMap", "Rol: $role")
        Log.d("RegisterActivityMap", "Email: $email")
        Log.d("RegisterActivityMap", "Contraseña: $password")

        // Al hacer click en el botón, recolectar la información y pasarla a la siguiente actividad
        findViewById<ImageView>(R.id.confirmButton).setOnClickListener {
            val localName = localNameEditText.text.toString()
            val maximumCapacityText = maximumCapacityEditText.text.toString()

            val maximumCapacity = maximumCapacityText.toIntOrNull()

            Log.d("RegisterActivityMap", "Nombre del local: $localName")
            Log.d("RegisterActivityMap", "Aforo máximo: $localName")

            singleMarker?.let {
                val latitude = it.position.latitude
                val longitude = it.position.longitude

                xcoord = latitude;
                ycoord = longitude;

                val user = User(
                    name = localName,
                    email = email.toString(),
                    password = password.toString(),
                    rol = role.toString(),
                               );



                val local = Local(
                    idUser = null, // Puedes asignar un valor predeterminado o hacerlo dinámico
                    name = localName,
                    description = "No description", // Puedes pasar un valor predeterminado o editar este campo
                    email = email.toString(),
                    password = password.toString(),
                    rol = role.toString(),
                    avg_rating = 0, // Puedes poner un valor predeterminado, o recibir este dato si es necesario
                    image_identifier = "default_image", // Valor predeterminado
                    capacity = maximumCapacity ?: 0, // Asegúrate de que este valor sea correcto
                    x_coord = xcoord, // Latitud del marcador
                    y_coord = ycoord // Longitud del marcador
                                 )

//asterisco subir a la base de datos asterisco negro negro negro negro negro negro negro negro negro negro NEGRATA TRANSEXUAL CON CHOCHO DE PLASTICO

                Log.d("RegisterActivityMap", "Latitud del marcador: $latitude")
                Log.d("RegisterActivityMap", "Longitud del marcador: $longitude")

                // Crear un intent para pasar todos los datos a la siguiente actividad
                val nextIntent = Intent(this, RegisterActivitySelectGenre::class.java)
                nextIntent.putExtra("rol", role) // Pasar el rol
                nextIntent.putExtra("email", email) // Pasar el email
                nextIntent.putExtra("password", password)
                nextIntent.putExtra("EXTRA_LATITUDE", latitude)
                nextIntent.putExtra("EXTRA_LONGITUDE", longitude)
                nextIntent.putExtra("nombreLocal", localName)
                nextIntent.putExtra("aforoMaximo", maximumCapacity)

                startActivity(nextIntent)
                finish()
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

    // Método para centrar el mapa en Barcelona
    private fun centerMapOnBarcelona() {
        val barcelona = GeoPoint(41.3784, 2.1925)  // Coordenadas de Barcelona
        osmMapView.controller.setCenter(barcelona)
        osmMapView.controller.setZoom(15.0)

        Log.d("RegisterActivityMap", "Permiso no concedido o ubicación no disponible. Centrado en Barcelona.")
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
