package com.ninjatech.classroomfinder.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ninjatech.classroomfinder.database.SavedSection
import com.ninjatech.classroomfinder.database.SavedSectionsDao
import kotlinx.coroutines.*

/**
 * ViewModel class for ProfileFragment.
 */
class ProfileViewModel(
    val database: SavedSectionsDao,
    app: Application) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val savedCourses = database.getAllSavedCourseData()

    /**
     * Cancel all coroutines when ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Delete a course from saved courses.
     */
    private suspend fun delete(savedSection: SavedSection) {
        withContext(Dispatchers.IO) {
            database.delete(savedSection)
        }
    }
}