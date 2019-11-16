package com.ninjatech.classroomfinder.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SectionCourseJoinDao {
    @Query("SELECT * FROM course_table WHERE remoteId = :courseId")
    fun loadSaved(courseId: Int): List<SectionCourseJoin>
}