package com.example.databasetest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "time_table", foreignKeys = arrayOf(
    ForeignKey(entity = Section::class,
        parentColumns = arrayOf("crn"),
        childColumns = arrayOf("crn"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Location::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("locId"),
        onDelete = ForeignKey.CASCADE)

))
data class TimeTable(
    @ColumnInfo(name = "crn") var crn: Int,
    @ColumnInfo(name = "locId") var locId: Int,
    @PrimaryKey @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "start_time") val timeStart: String?,
    @ColumnInfo(name = "end_time") val timeEnd: String?,
    @ColumnInfo(name = "day") val day: String?,
    @ColumnInfo(name = "start_day") var startDay: String,
    @ColumnInfo(name = "end_day") var endDay: String
)