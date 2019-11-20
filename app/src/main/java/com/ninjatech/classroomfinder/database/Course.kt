package com.ninjatech.classroomfinder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses"
)
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val subject: String,
    val title: String
)
