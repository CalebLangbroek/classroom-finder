package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CourseDao {
    @Query("SELECT course_table.*, section_table.* FROM section_table INNER JOIN course_table ON section_table.courseId = course_table.id ORDER BY title ASC")
    fun getAlphabetizedCourses(): LiveData<List<SectionAndCourse>>?

    /**
     * Query the database for a match on the title or subject.
     * Using the % operator so it can match on either side.
     */
    @Query("SELECT course_table.*, section_table.* FROM section_table INNER JOIN course_table ON section_table.courseId = course_table.id WHERE course_table.title LIKE '%' || :query || '%' OR course_table.subject LIKE '%' || :query || '%'")
    fun getCourseByTitleOrSubject(query : String) : LiveData<List<SectionAndCourse>>?

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