package com.ninjatech.classroomfinder.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ninjatech.classroomfinder.database.CoursesDao
import com.ninjatech.classroomfinder.database.SavedSection
import kotlinx.coroutines.*

/**
 * ViewModel class for SearchFragment.
 */
class SearchViewModel(
    val database: CoursesDao,
    app: Application
) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val searchInput = MutableLiveData("")

    private var _snackBarText = MutableLiveData<String>()

    val snackBarText: LiveData<String> get() = _snackBarText

    var courses = Transformations.switchMap(searchInput) {
        if (it.isEmpty()) {
            // Return all courses if there isn't a search value
            database.getAllCourses()
        } else {
            // Otherwise filter the courses
            database.getCourseByTitleOrSubject(it)
        }
    }

    /**
     * Cancel all coroutines when ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Filter the course list based on a search query.
     */
    fun filterCourses(query: String?) {
        // Update the search value
        searchInput.value = query
    }

    /**
     * Called when a course had been clicked.
     */
    fun onCourseClicked(crn: Int) {
        uiScope.launch {
            val savedSection = getSavedSectionByCrn(crn)

            if (savedSection != null) {
                // Remove from saved list
                deleteSavedSection(savedSection)
                _snackBarText.value = "Course Removed"
            } else {
                // Add to saved list
                insertSavedSection(SavedSection(0, crn))
                _snackBarText.value = "Course Added"
            }
        }
    }

    fun clearSnackBarText() {
        _snackBarText.value = ""
    }

    private suspend fun getSavedSectionByCrn(crn: Int): SavedSection? {
        return withContext(Dispatchers.IO) {
            val isSaved = database.getSavedSectionByCrn(crn)
            isSaved
        }
    }

    private suspend fun insertSavedSection(newSavedSection: SavedSection): Long {
        return withContext(Dispatchers.IO) {
            val rowsEffected = database.insertSavedSection(newSavedSection)
            rowsEffected
        }
    }

    private suspend fun deleteSavedSection(savedSection: SavedSection): Int {
        return withContext(Dispatchers.IO) {
            val rowsEffected = database.deleteSavedSection(savedSection)
            rowsEffected
        }
    }

}