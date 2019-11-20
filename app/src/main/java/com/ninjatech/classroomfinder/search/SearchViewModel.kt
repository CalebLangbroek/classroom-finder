package com.ninjatech.classroomfinder.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ninjatech.classroomfinder.database.CourseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * ViewModel class for SearchFragment.
 */
class SearchViewModel(
    val database: CourseDao,
    app: Application
) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val searchInput = MutableLiveData("")

    var courses = Transformations.switchMap(searchInput) {
        if(it.isEmpty()) {
            // Return all courses if there isn't a search value
            database.getAlphabetizedCourses()
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

}