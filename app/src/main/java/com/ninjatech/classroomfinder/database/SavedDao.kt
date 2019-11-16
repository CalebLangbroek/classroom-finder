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

    @Query("SELECT * FROM saved_course_table ORDER BY localId ASC")
    fun getAll(): LiveData<List<SavedCourse>>

//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT * FROM saved_course_table NATURAL JOIN section_table INNER JOIN course_table ON section_table.courseId=course_table.id")
//    fun getAllSavedCourseData(): LiveData<List<SavedCourse>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM saved_course_table NATURAL JOIN time_table INNER JOIN location_table ON time_table.locationId=location_table.id")
    fun getAllSavedCourseTime(): LiveData<List<SavedCourse>>

    @Query("DELETE FROM saved_course_table")
    fun deleteAll()
}