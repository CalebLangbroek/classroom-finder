package com.ninjatech.classroomfinder.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "saved_list_table",
    foreignKeys = arrayOf(
    ForeignKey(entity = Section::class,
        parentColumns = arrayOf("crn"),
        childColumns = arrayOf("crn"),
        onDelete = ForeignKey.CASCADE))
)
data class SavedList(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var crn: Int
)