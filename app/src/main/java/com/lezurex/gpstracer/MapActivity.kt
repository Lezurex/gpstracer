package com.lezurex.gpstracer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lezurex.gpstracer.util.LocationPermissionHelper
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)

        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }

        val intent = Intent(applicationContext, LocationService::class.java)
        startService(intent)
    }

    private fun onMapReady() {
        mapView.getMapboxMap().loadStyleUri("mapbox://styles/lezurex/clf9trxio00d201q6frjc01tu") {
            mapView.location.updateSettings {
                enabled = true
                pulsingEnabled = true
            }
        }
    }
}