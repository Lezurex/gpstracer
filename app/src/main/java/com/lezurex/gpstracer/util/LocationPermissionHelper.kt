package com.lezurex.gpstracer.util

import android.app.Activity
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import java.lang.ref.WeakReference

/**
 * The LocationPermissionHelper is taken from an official Mapbox example.
 * https://github.com/mapbox/mapbox-maps-android/blob/b35837b8e7c85df8c5476056f065d38ea56d55a0/app/src/main/java/com/mapbox/maps/testapp/utils/LocationPermissionHelper.kt
 */
class LocationPermissionHelper(val activity: WeakReference<Activity>) {
    private lateinit var permissionsManager: PermissionsManager

    fun checkPermissions(onMapReady: () -> Unit) {
        if (PermissionsManager.areLocationPermissionsGranted(activity.get())) {
            onMapReady()
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    Toast.makeText(
                        activity.get(), "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        onMapReady()
                    } else {
                        activity.get()?.finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(activity.get())
        }
    }
}