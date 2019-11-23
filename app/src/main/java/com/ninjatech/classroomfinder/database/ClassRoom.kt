package com.ninjatech.classroomfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "rooms", foreignKeys = arrayOf(
    ForeignKey(
        entity = Coordinate::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("coor_id")
    )
))
data class ClassRoom(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "coor_id")
    val coorId : String,
    val building: String,
    val level : Int
)