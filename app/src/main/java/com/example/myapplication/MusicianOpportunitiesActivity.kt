package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MusicianOpportunitiesActivity: AppCompatActivity() {
    private lateinit var osmMapView: MapView

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        //const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        //const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
    }

    private val TAG = "MusicianOpportunities"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_opportunities_map_actitity)

        osmMapView = findViewById(R.id.osmMapView)
        osmMapView.setMultiTouchControls(true)

        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))

        centerMapOnBarcelona()

        testApiConnection()
        checkLocationPermission()


    }

    private fun testApiConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.eventService.getEvents()

                withContext(Dispatchers.Main) {
                    Log.d(TAG, "Respuesta recibida: ${response.size} eventos")

                    response.forEach { event ->
                        val lat = event.Local.y_coordination
                        val lon = event.Local.x_coordination
                        val name = event.Local.name

                        addEventMarker(lat, lon, name)

                        Log.d(TAG, "Evento: $name en ($lat, $lon)")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "Error al conectar con la API: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            setUserLocation()
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

                Log.d("MusicianOpportunitiesActivity", "Ubicación del usuario: ${it.latitude}, ${it.longitude}")
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
            osmMapView.controller.animateTo(barcelona)

            // Alternativa más directa:
            // osmMapView.controller.setCenter(barcelona)
            // osmMapView.controller.setZoom(15.0)

            Log.d(TAG, "Mapa centrado en Barcelona: ${barcelona.latitude}, ${barcelona.longitude}")
        } catch (e: Exception) {
            Log.e(TAG, "Error al centrar mapa: ${e.message}")
        }
    }
    private fun addEventMarker(latitude: Double, longitude: Double, description: String) {
        val eventLocation = GeoPoint(latitude, longitude)
        val marker = Marker(osmMapView)
        marker.position = eventLocation
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = description
        osmMapView.overlays.add(marker)
        osmMapView.invalidate() // refresca el mapa
    }
}