package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CourseDao {
    @Query("SELECT * from course_table ORDER BY title ASC")
    fun getAlphabetizedCourses(): LiveData<List<Course>>

    @Query("SELECT * from course_table ORDER BY title ASC")
    fun getAll(): LiveData<List<Course>>

    @Query("DELETE FROM course_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(course: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Course): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(vararg entity: Course)

    @Update
    suspend fun update(entity: Course)

    @Update
    fun updateAll(vararg entity: Course)
}