package com.ninjatech.classroomfinder.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ninjatech.classroomfinder.database.SavedCourse
import com.ninjatech.classroomfinder.database.SavedDao
import kotlinx.coroutines.*

/**
 * ViewModel class for ProfileFragment.
 */
class ProfileViewModel(
    val database: SavedDao,
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
    private suspend fun delete(course: SavedCourse) {
        withContext(Dispatchers.IO) {
            database.delete(course)
        }
    }
}