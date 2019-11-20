package com.ninjatech.classroomfinder.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ninjatech.classroomfinder.database.CoursesDao
import java.lang.IllegalArgumentException


/**
 * ViewModel Factory for creating ViewModel for SearchFragment.
 */
class SearchViewModelFactory(
    private val database: CoursesDao,
    private val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // Make sure the SearchViewModel class exists
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            // Create a view model
            return SearchViewModel(database, app) as T
        } else {
            throw IllegalArgumentException("ViewModel class doesn't exist")
        }
    }

}