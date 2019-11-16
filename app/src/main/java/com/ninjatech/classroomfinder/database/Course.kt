package com.ninjatech.classroomfinder.database

import androidx.room.*


@Entity(
    tableName = "course_table",
    foreignKeys = [ ForeignKey(
        entity = SavedCourse::class,
        parentColumns = arrayOf("remoteId"),
        childColumns = arrayOf("saveId"),
        onDelete = ForeignKey.CASCADE)],
    indices = [
        Index(value = arrayOf("saveId")),
        Index(value = arrayOf("remoteId"), unique = true)]
    )

data class Course(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localId")
    var id: Int,
    var remoteId: Int,
    var saveId: Int,
    var title: String,
    var subject: String
)
