package com.ninjatech.classroomfinder

import android.app.Application
import android.location.Location
import androidx.lifecycle.*

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private var location = LocationLiveData(application)

    fun getCurrentLocation() = location
}



