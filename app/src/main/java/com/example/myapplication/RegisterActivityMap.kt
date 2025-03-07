package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
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

class RegisterActivityMap : AppCompatActivity() {
    private lateinit var osmMapView: MapView
    private var singleMarker: Marker? = null


    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity3local)


        Configuration.getInstance().userAgentValue = packageName
        osmMapView = findViewById(R.id.osmMapView)
        osmMapView.setMultiTouchControls(true)

        val role = intent.getStringExtra("rol")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val nombreLocalEditText = findViewById<EditText>(R.id.nombreLocalEditText)
        val aforoMaximoEditText = findViewById<EditText>(R.id.aforoMaximoEditText)

        checkLocationPermission()
        setupMapTouchOverlay()

        findViewById<ImageView>(R.id.confirmButton).setOnClickListener {
            val nombreLocal = nombreLocalEditText.text.toString()
            val aforoMaximo = aforoMaximoEditText.text.toString()

            singleMarker?.let {
                val latitude = it.position.latitude
                val longitude = it.position.longitude

                val nextIntent = Intent(this, RegisterActivity1::class.java)

                nextIntent.putExtra("rol", role)
                nextIntent.putExtra("email", email)
                nextIntent.putExtra("password", password)
                nextIntent.putExtra(EXTRA_LATITUDE, latitude)
                nextIntent.putExtra(EXTRA_LONGITUDE, longitude)
                nextIntent.putExtra(nombreLocal, nombreLocal)
                nextIntent.putExtra(aforoMaximo, aforoMaximo.toInt())


                startActivity(nextIntent)
                finish()
            }
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
                }
                return true
            }
            override fun longPressHelper(p: GeoPoint?) = false
        }))
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
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setUserLocation()
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
