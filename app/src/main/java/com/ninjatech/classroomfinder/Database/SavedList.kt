package com.example.databasetest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "saved", foreignKeys = arrayOf(
    ForeignKey(entity = Section::class,
        parentColumns = arrayOf("crn"),
        childColumns = arrayOf("crn"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Section::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("timeId"),
        onDelete = ForeignKey.CASCADE)
))
data class SavedList(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "timeId") var time: Int,
    @ColumnInfo(name = "crn") var crn: Int
)