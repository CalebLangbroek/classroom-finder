package com.ninjatech.classroomfinder.database

import androidx.room.*


@Entity(
    tableName = "section_table", foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("courseId"),
            onDelete = ForeignKey.CASCADE
        )], indices = [
        Index(value = arrayOf("courseId")),
        Index(value = arrayOf("crn"), unique = true)]
    )
data class Section(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "crn")
    var id: Int,
//    var remoteId: Int,
    var title: String,
    var courseId: Int
)





