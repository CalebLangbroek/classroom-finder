package com.ninjatech.classroomfinder.database

import androidx.room.*

@Entity(
    tableName = "sections", foreignKeys = arrayOf(
        ForeignKey(
            entity = Course::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("course_id")
        )
    )
)
data class Section(
    @PrimaryKey
    val crn: Int,

    @ColumnInfo(name = "course_id")
    val courseId: Int,
    val title: String
)





