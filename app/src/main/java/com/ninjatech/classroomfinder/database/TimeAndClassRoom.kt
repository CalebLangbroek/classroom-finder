package com.ninjatech.classroomfinder.database

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

/**
 * Database View for joining time and classroom.
 */
@DatabaseView(
    "SELECT * FROM times INNER JOIN rooms ON times.room_id = rooms.id"
)
data class TimeAndClassRoom(
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
    val endDate: String,

    @ColumnInfo(name = "coor_id")
    val coorId : String,

    val building: String,

    val level : Int
)