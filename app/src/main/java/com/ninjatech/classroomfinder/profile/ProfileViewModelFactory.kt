package com.ninjatech.classroomfinder.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ninjatech.classroomfinder.database.SavedDao
import java.lang.IllegalArgumentException


/**
 * ViewModel Factory for creating ViewModel for ProfileFragment.
 */
class ProfileViewModelFactory(
    private val database: SavedDao,
    private val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // Make sure the ProfileViewModel class exists
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            // Create a view model
            return ProfileViewModel(database, app) as T
        } else {
            throw IllegalArgumentException("ViewModel class doesn't exist")
        }
    }

}