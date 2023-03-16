package com.lezurex.gpstracer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.lezurex.gpstracer.domain.AppDatabase
import com.lezurex.gpstracer.domain.dao.PointDao
import com.lezurex.gpstracer.domain.entity.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class LocationService : Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var db: AppDatabase
    private lateinit var pointDao: PointDao

    private val locationServiceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "gpstracer").build()
        pointDao = db.pointDao()

        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000 * 10, 0f, this
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
        db.close()
    }

    override fun onLocationChanged(location: Location) {
        var point = Point(
            LocalDateTime.now(), location.longitude, location.latitude, location.accuracy.toDouble()
        )
        locationServiceScope.launch { pointDao.insertAll(point) }
        Log.i("sus", point.toString())
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "gpstracer_service").setContentTitle("GPS Tracer")
            .setContentText("Big brother is watching you ;D")
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentIntent(pendingIntent).build()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
