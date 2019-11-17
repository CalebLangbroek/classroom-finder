package com.ninjatech.classroomfinder

import android.location.Location
import androidx.lifecycle.*

class MapViewModel: ViewModel() {
    private var location = LocationLiveData

    fun getCurrentLocation() = location
}



