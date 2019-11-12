package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SavedDao {

    @Insert
    fun insert(savedCourse: SavedCourse)

    @Delete
    fun delete(savedCourse: SavedCourse)

    @Update
    fun update(savedCourse: SavedCourse)

    @Query("SELECT * FROM saved_course_table ORDER BY crn ASC")
    fun getAll(): LiveData<List<SavedCourse>>


    @Query("SELECT * FROM saved_course_table NATURAL JOIN section_table ORDER BY crn ASC")
    fun getAllSavedCoursesSectionData() : LiveData<List<SavedCourse>>

    @Query("DELETE FROM saved_course_table")
    fun deleteAll()
}