package com.example.databasetest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    @PrimaryKey@ColumnInfo(name = "locId") var id: Int,
    @ColumnInfo(name = "long") var longitude: Double,
    @ColumnInfo(name = "lat") var latitude: Double,
    @ColumnInfo(name = "building") var building: String,
    @ColumnInfo(name = "room") var room: String
)