package com.example.databasetest

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SavedDao {
    @Query("SELECT * from saved ORDER BY crn ASC")
    fun getAlphabetizedCourses(): LiveData<List<SavedList>>

    @Query("DELETE FROM course")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(course: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Course) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertCourse(vararg entity: Course)

    @Update
    suspend fun update(entity: Course)

    @Update
    fun updateAll(vararg entity: Course)
}