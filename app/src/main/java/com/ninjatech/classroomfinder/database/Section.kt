package com.ninjatech.classroomfinder.database

import androidx.room.*


@Entity(
    tableName = "section_table", foreignKeys = arrayOf(
        ForeignKey(
            entity = Course::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("courseId"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Section(
    @PrimaryKey
    var crn: Int,

    @ColumnInfo(name = "section_title")
    var title: String,
    var courseId: Int
)





