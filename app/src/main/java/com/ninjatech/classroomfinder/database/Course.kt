package com.ninjatech.classroomfinder.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "course_table"
)
data class Course(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var subject: String
)
