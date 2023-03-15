package com.lezurex.gpstracer

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lezurex.gpstracer.util.LocationPermissionHelper
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.eq
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.get
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.literal
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.FillExtrusionLayer
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
    }

    private fun onMapReady() {
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->
            mapView.location.updateSettings {
                enabled = true
                pulsingEnabled = true
            }
            setupBuildings(style)
        }
    }

    private fun setupBuildings(style: Style) {
        val fillExtrusionLayer = FillExtrusionLayer("3d-buildings", "composite")
        fillExtrusionLayer.sourceLayer("building")
        fillExtrusionLayer.filter(eq(get("extrude"), literal("true")))
        fillExtrusionLayer.minZoom(10.0)
        fillExtrusionLayer.fillExtrusionColor(Color.parseColor("#aaaaaa"))
        fillExtrusionLayer.fillExtrusionHeight(get("height"))
        fillExtrusionLayer.fillExtrusionBase(get("min_height"))
        fillExtrusionLayer.fillExtrusionOpacity(0.6)
        fillExtrusionLayer.fillExtrusionAmbientOcclusionIntensity(0.3)
        fillExtrusionLayer.fillExtrusionAmbientOcclusionRadius(3.0)
        style.addLayer(fillExtrusionLayer)
    }

}