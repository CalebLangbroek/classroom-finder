package com.ninjatech.classroomfinder.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context) : LiveData<LocationModel>() {

    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.also {
                setLocation(it)
            }
        }
        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun setLocation(location: Location) {
        value = LocationModel(
            longitude = location.longitude,
            latitude = location.latitude
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocation(location)
            }
        }
    }
}

data class LocationModel(
    val longitude: Double,
    val latitude: Double
)