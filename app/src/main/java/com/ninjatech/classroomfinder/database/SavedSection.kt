package com.ninjatech.classroomfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_sections",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Section::class,
            parentColumns = arrayOf("crn"),
            childColumns = arrayOf("section_crn")
        )
    )
)
data class SavedSection(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "saved_section_id")
    val id: Int,

    @ColumnInfo(name = "section_crn")
    val sectionCrn: Int
)