package com.lezurex.gpstracer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class GpsTracerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            "gpstracer_service",
            "GPS Tracer Location Service",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}