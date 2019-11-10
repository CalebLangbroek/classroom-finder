package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SavedDao {

    @Insert
    fun insert(savedList: SavedList)

    @Delete
    fun delete(savedList: SavedList)

    @Update
    fun update(savedList: SavedList)

    @Query("SELECT * from saved_list_table ORDER BY crn ASC")
    fun getAlphabetizedCourses(): LiveData<List<SavedList>>

    @Query("DELETE FROM saved_list_table")
    fun deleteAll()
}