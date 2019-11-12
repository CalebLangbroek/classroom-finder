package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SectionDao {

    @Query("SELECT * from section_table ORDER BY crn ASC")
    fun getAlphabetizedCourses(): LiveData<List<Section>>

    @Query("DELETE FROM section_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(section: Section)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Section) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insertCourse(vararg entity: Section)

    @Update
    suspend fun update(entity: Section)

    @Update
    fun updateAll(vararg entity: Section)
}