package com.ninjatech.classroomfinder.database

import androidx.room.*


@Entity(
    tableName = "time_table", foreignKeys = arrayOf(
        ForeignKey(
            entity = Section::class,
            parentColumns = arrayOf("crn"),
            childColumns = arrayOf("crn")
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("locationId")
        )
    ),
            indices = [
            Index(value = arrayOf("crn")),
//            Index(value = arrayOf("remoteId"), unique = true),
            Index(value = arrayOf("locationId"))]
)
data class Time(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
//    var sectionId: Int,
//    var remoteId: Int,
    var crn: Int,
    var locationId: Int,
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