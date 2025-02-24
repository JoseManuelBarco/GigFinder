package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.events.MapEventsReceiver
import android.widget.Button

class RegisterActivityMap : AppCompatActivity() {
    private lateinit var osmMapView: MapView
    private lateinit var locationManager: LocationManager
    private var singleMarker: Marker? = null

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Centramos el mapa en la ubicación del usuario
            val userLat = location.latitude
            val userLon = location.longitude
            centerMapOnLocation(userLat, userLon)

            // Removemos las actualizaciones si solo necesitamos la primera
            locationManager.removeUpdates(this)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity3local)

        // Configura el user agent para osmdroid
        Configuration.getInstance().userAgentValue = packageName

        // Referencia al MapView
        osmMapView = findViewById(R.id.osmMapView)

        // Inicializa el locationManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Verifica permisos de ubicación
        checkLocationPermission()

        // Configura el mapa
        setupMap()

        // Configura el overlay para toques en el mapa
        setupMapTouchOverlay()

        // Configurar botón de Confirmar
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            // Si existe un marcador, obtenemos su posición y la pasamos a la siguiente Activity
            singleMarker?.let { marker ->
                val lat = marker.position.latitude
                val lon = marker.position.longitude

                // Crea un intent para pasar datos a otra Activity
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_LATITUDE, lat)
                    putExtra(EXTRA_LONGITUDE, lon)
                }

                // Podrías usar startActivity(intent) si vas a abrir otra Activity
                // o setResult(...) si vas a terminar esta Activity y retornar datos
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun setupMap() {
        // Habilita el zoom con multi-touch
        osmMapView.setMultiTouchControls(true)
        // Zoom inicial
        osmMapView.controller.setZoom(10.0)
        // Centro inicial (si no hay ubicación)
        osmMapView.controller.setCenter(GeoPoint(37.7749, -122.4194))

        // (Opcional) Overlay de ubicación del usuario
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), osmMapView)
        locationOverlay.enableMyLocation()
        osmMapView.overlays.add(locationOverlay)
    }

    /**
     * Configura un overlay para capturar eventos de toque en el mapa.
     * Cuando el usuario toque el mapa, crearemos o moveremos un único marcador.
     */
    private fun setupMapTouchOverlay() {
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let { geoPoint ->
                    // Si no existe un marcador aún, lo creamos
                    if (singleMarker == null) {
                        singleMarker = Marker(osmMapView).apply {
                            position = geoPoint
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = "Ubicación seleccionada"
                        }
                        osmMapView.overlays.add(singleMarker)
                    } else {
                        // Si ya existe, simplemente movemos el marcador a la nueva ubicación
                        singleMarker?.position = geoPoint
                    }
                    // Redibujamos el mapa
                    osmMapView.invalidate()
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                // No hacemos nada en longPress, pero podrías añadir funcionalidad extra
                return false
            }
        })
        osmMapView.overlays.add(mapEventsOverlay)
    }

    private fun centerMapOnLocation(lat: Double, lon: Double) {
        val userLocation = GeoPoint(lat, lon)
        osmMapView.controller.setCenter(userLocation)
        osmMapView.controller.setZoom(15.0)
    }

    private fun checkLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_LOCATION_PERMISSION)
        } else {
            // Si ya está concedido, solicitamos la ubicación
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L, 0f,
                locationListener
                                                  )
            // También podemos intentar obtener la última ubicación conocida
            val lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnown != null) {
                centerMapOnLocation(lastKnown.latitude, lastKnown.longitude)
            }
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                // Permiso denegado
            }
        }
    }

    // Ciclo de vida del MapView
    override fun onResume() {
        super.onResume()
        osmMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        osmMapView.onPause()
    }
}
