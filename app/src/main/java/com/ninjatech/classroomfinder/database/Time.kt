package com.ninjatech.classroomfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "time_table", foreignKeys = arrayOf(
        ForeignKey(
            entity = Section::class,
            parentColumns = arrayOf("crn"),
            childColumns = arrayOf("crn"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("locationId"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Time(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var crn: Int,
    var locationId: Int?,
    val day: String,

    @ColumnInfo(name = "start_time")
    val startTime: String,

    @ColumnInfo(name = "end_time")
    val endTime: String,

    @ColumnInfo(name = "start_date")
    var startDate: String,

    @ColumnInfo(name = "end_date")
    var endDate: String
)