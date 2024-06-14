package com.bangkit.glowfyapp.view.home.fragments.clinic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

class LocationChangeReceiver(private val listener: LocationChangeListener) : BroadcastReceiver() {

    interface LocationChangeListener {
        fun onLocationEnabled()
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                listener.onLocationEnabled()
            }
        }
    }
}