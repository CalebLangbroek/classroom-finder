package com.ninjatech.classroomfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Location(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "long")
    var longitude: Double,

    @ColumnInfo(name = "lat")
    var latitude: Double,

    var building: String?,
    var room: String?
)