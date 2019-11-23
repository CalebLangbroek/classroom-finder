package com.ninjatech.classroomfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reachable_coordinates", foreignKeys = arrayOf(
        ForeignKey(
            entity = Coordinate::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("from_coor_id")
        ),
        ForeignKey(
            entity = Coordinate::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("to_coor_id")
        )
    )
)
data class ReachableCoordinate(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "from_coor_id")
    var fromCoorId: String,

    @ColumnInfo(name = "to_coor_id")
    var toCoorId: String,

    var cost: Float
)