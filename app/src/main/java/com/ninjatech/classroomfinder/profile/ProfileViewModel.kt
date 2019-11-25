package com.ninjatech.classroomfinder.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ninjatech.classroomfinder.database.Coordinate
import com.ninjatech.classroomfinder.database.SavedSectionsDao
import kotlinx.coroutines.*

/**
 * ViewModel class for ProfileFragment.
 */
class ProfileViewModel(
    val database: SavedSectionsDao,
    app: Application
) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _navigateToMap = MutableLiveData<Coordinate>()

    val navigateToMap: LiveData<Coordinate>
        get() = _navigateToMap

    private var _snackBarText = MutableLiveData<String>()

    val snackBarText: LiveData<String>
        get() = _snackBarText

    val savedCourses = database.getAllSavedCourseData()


    /**
     * Cancel all coroutines when ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Called when the navigation button beside a course is clicked.
     */
    fun onNavigateButtonClicked(crn: Int) {
        uiScope.launch {
            val coordinate = getCoordinateFromCrn(crn)

            _navigateToMap.value = coordinate
        }
    }

    /**
     * Called when the delete button is clicked, deletes the saved section.
     */
    fun onDeleteButtonClicked(crn: Int) {
        uiScope.launch {
            deleteSavedSectionByCrn(crn)
            _snackBarText.value = "Course Removed"
        }
    }

    /**
     * Called after navigation is finished to reset the navigateToMap variable.
     */
    fun navigationFinished() {
        _navigateToMap.value = null
    }

    fun clearSnackBarText() {
        _snackBarText.value = ""
    }

    /**
     * Delete a course from saved courses.
     */
    private suspend fun deleteSavedSectionByCrn(crn: Int) {
        withContext(Dispatchers.IO) {
            database.deleteSavedSectionByCrn(crn)
        }
    }

    /**
     * Get the coordinate from a section crn.
     */
    private suspend fun getCoordinateFromCrn(crn: Int): Coordinate? {
        return withContext(Dispatchers.IO) {
            return@withContext database.getCoordinateFromCrn(crn)
        }
    }
}