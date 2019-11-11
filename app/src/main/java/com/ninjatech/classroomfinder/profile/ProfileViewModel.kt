package com.ninjatech.classroomfinder.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ninjatech.classroomfinder.database.SavedDao

/**
 * ViewModel class for ProfileFragment.
 */
class ProfileViewModel(
    val database: SavedDao,
    app: Application) : AndroidViewModel(app) {
}