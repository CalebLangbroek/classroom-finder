package com.ninjatech.classroomfinder.database

import androidx.room.*


@Entity(
    tableName = "course_table",
//    foreignKeys = [ ForeignKey(
//        entity = SavedCourse::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("id"),
//        onDelete = ForeignKey.CASCADE)],
    indices = [
//        Index(value = arrayOf("id")),
        Index(value = arrayOf("id"), unique = true)]
    )

data class Course(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
//    var remoteId: Int,
//    var saveId: Int,
    var title: String,
    var subject: String
)
