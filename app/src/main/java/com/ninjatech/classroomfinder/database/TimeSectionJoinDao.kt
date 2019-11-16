package com.ninjatech.classroomfinder.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TimeSectionJoinDao {
    @Query("SELECT * FROM course_table WHERE crn = :id")
    fun loadSaved(id: Int): List<TimeSectionJoin>
}