package com.ninjatech.classroomfinder.database

import androidx.room.*


@Entity(
    tableName = "section_table", foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = arrayOf("remoteId"),
            childColumns = arrayOf("courseId"),
            onDelete = ForeignKey.CASCADE
        )], indices = [
        Index(value = arrayOf("courseId")),
        Index(value = arrayOf("remoteId"), unique = true)]
    )
data class Section(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "localId")
    var id: Int,
    var remoteId: Int,
    var title: String,
    var courseId: Int
)





