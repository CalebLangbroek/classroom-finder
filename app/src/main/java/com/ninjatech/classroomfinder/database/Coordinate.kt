package com.ninjatech.classroomfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "coordinates"
)
data class Coordinate(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "lat")
    var latitude: Float,

    @ColumnInfo(name = "long")
    var longitude: Float
)
