package com.bangkit.glowfyapp.data.models

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

data class ClinicData (
    val name: String,
    val address: String,
    val location: LatLng,
    val icon: Bitmap?,
    val markerOptions: MarkerOptions
)