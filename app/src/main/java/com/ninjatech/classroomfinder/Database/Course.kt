package com.example.databasetest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "course")
data class Course (
    @PrimaryKey
    var title: String,
    var subject: String
)
