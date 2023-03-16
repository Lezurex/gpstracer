package com.lezurex.gpstracer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.lezurex.gpstracer.domain.AppDatabase
import com.lezurex.gpstracer.domain.dao.PointDao
import com.lezurex.gpstracer.util.LocationPermissionHelper
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private lateinit var mapView: MapView
    private lateinit var pointDao: PointDao

    private val mapActivityScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "gpstracer").build()
        pointDao = db.pointDao()

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
        loadPoints()
    }

    private fun loadPoints() = mapActivityScope.launch {
        val points = withContext(Dispatchers.IO) { pointDao.getAll() }
        Log.i("MapActivity", points.toString())

        withContext(Dispatchers.Main) {
            addPointsToMap(points)
        }
    }

    private fun addPointsToMap(points: List<com.lezurex.gpstracer.domain.entity.Point>) {
        val featureCollection = FeatureCollection.fromFeatures(points.map { point ->
            Feature.fromGeometry(Point.fromLngLat(point.lon, point.lat))
        })

        Log.i("MapActivity", featureCollection.toString())

        val geoJsonSource = GeoJsonSource.Builder("point-source")
            .featureCollection(featureCollection)
            .build()

        mapView.getMapboxMap().getStyle { style ->
            style.addSource(geoJsonSource)

            val layer = SymbolLayer("point-layer", "point-source")
            layer.iconImage("border-dot-13")
            layer.iconColor("red")
            layer.iconSize(2.0)
            style.addLayer(layer)
        }
    }

}