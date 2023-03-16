package com.lezurex.gpstracer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.lezurex.gpstracer.domain.AppDatabase
import com.lezurex.gpstracer.domain.dao.PointDao
import com.lezurex.gpstracer.util.LocationPermissionHelper
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private lateinit var mapView: MapView
    private lateinit var pointDao: PointDao
    private lateinit var geoJsonSource: GeoJsonSource

    private val pointsLiveData: LiveData<List<com.lezurex.gpstracer.domain.entity.Point>> by lazy {
        pointDao.getAll()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val db = AppDatabase.getDatabase(applicationContext)
        pointDao = db.pointDao()

        mapView = findViewById(R.id.mapView)

        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }

        val intent = Intent(applicationContext, LocationService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        pointsLiveData.removeObservers(this)
    }

    private fun onMapReady() {
        mapView.getMapboxMap().loadStyleUri("mapbox://styles/lezurex/clf9trxio00d201q6frjc01tu") {
            mapView.location.updateSettings {
                enabled = true
                pulsingEnabled = true
            }
            addPointsLayer()
            updateMapWithLatestPoints()
        }
    }

    private fun updateMapWithLatestPoints() {
        pointsLiveData.observe(this) { points ->
            addPointsToMap(points)
        }
    }

    private fun addPointsToMap(points: List<com.lezurex.gpstracer.domain.entity.Point>) {
        val featureList = mutableListOf<Feature>()
        val pointList = mutableListOf<Point>()

        for (point in points) {
            featureList.add(Feature.fromGeometry(Point.fromLngLat(point.lon, point.lat)))
            pointList.add(Point.fromLngLat(point.lon, point.lat))
        }
        val lineString = LineString.fromLngLats(pointList)

        geoJsonSource.featureCollection(FeatureCollection.fromFeatures(featureList))
        geoJsonSource.geometry(lineString)
    }

    private fun addPointsLayer() {
        geoJsonSource = GeoJsonSource.Builder("point-source").build()

        mapView.getMapboxMap().getStyle { style ->
            style.addSource(geoJsonSource)

            val lineLayer = LineLayer("line-layer", "point-source")
            lineLayer.lineColor("red")
            lineLayer.lineWidth(4.0)
            lineLayer.sourceLayer("point-layer")
            style.addLayer(lineLayer)

            val pointsLayer = SymbolLayer("point-layer", "point-source")
            pointsLayer.iconImage("border-dot-13")
            pointsLayer.iconColor("red")
            pointsLayer.iconSize(2.0)
            style.addLayer(pointsLayer)
        }
    }

}