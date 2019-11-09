package com.ninjatech.classroomfinder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "section", foreignKeys = arrayOf(ForeignKey(entity = Course::class,
    parentColumns = arrayOf("title"),
    childColumns = arrayOf("title"),
    onDelete = ForeignKey.CASCADE)))
data class Section(
    @PrimaryKey@ColumnInfo(name = "crn") var crn: Int,
    @ColumnInfo(name = "title") var title: String
)





