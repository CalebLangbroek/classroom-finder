package com.ninjatech.classroomfinder.map

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ninjatech.classroomfinder.database.CoordinateDao
import java.lang.IllegalArgumentException

/**
 * ViewModel Factory for creating ViewModel for MapFragment.
 */
class MapViewModelFactory(
    private val database: CoordinateDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // Make sure the MapViewModel class exists
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            // Create a view model
            return MapViewModel(database, application) as T
        } else {
            throw IllegalArgumentException("ViewModel class doesn't exist")
        }
    }
}