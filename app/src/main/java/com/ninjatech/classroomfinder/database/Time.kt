package com.ninjatech.classroomfinder.database

import androidx.room.*

@Entity(
    tableName = "times", foreignKeys = arrayOf(
        ForeignKey(
            entity = Section::class,
            parentColumns = arrayOf("crn"),
            childColumns = arrayOf("section_crn")
        ),
        ForeignKey(
            entity = ClassRoom::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("room_id")
        )
    )
)
data class Time(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "section_crn")
    val sectionCrn: Int,

    @ColumnInfo(name = "room_id")
    val roomId: String,

    val day: String,

    @ColumnInfo(name = "start_time")
    val startTime: String,

    @ColumnInfo(name = "end_time")
    val endTime: String,

    @ColumnInfo(name = "start_date")
    val startDate: String,

    @ColumnInfo(name = "end_date")
    val endDate: String
)