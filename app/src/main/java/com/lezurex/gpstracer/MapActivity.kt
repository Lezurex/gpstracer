package com.lezurex.gpstracer

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.eq
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.get
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.literal
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.FillExtrusionLayer

var mapView: MapView? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapView)
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS) { style ->
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