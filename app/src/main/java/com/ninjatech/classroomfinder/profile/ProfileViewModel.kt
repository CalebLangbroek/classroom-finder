package com.ninjatech.classroomfinder.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ninjatech.classroomfinder.database.SavedSection
import com.ninjatech.classroomfinder.database.SavedSectionsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

/**
 * ViewModel class for ProfileFragment.
 */
class ProfileViewModel(
    val database: SavedSectionsDao,
    app: Application
) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _editEnabled = MutableLiveData<Boolean>(false)
    val editEnabled: LiveData<Boolean> get() = _editEnabled

    val savedCourses = database.getAllSavedCourseData()


    /**
     * Cancel all coroutines when ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Enable or disable editing.
     */
    fun onEditButtonClicked() {

    }

    /**
     *
     */
    fun onNavigateButtonClicked() {

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