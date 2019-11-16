package com.ninjatech.classroomfinder.database

import androidx.room.*

//@Entity(
//    tableName = "saved_course_table",
//    foreignKeys = arrayOf(
//        ForeignKey(
//            entity = Section::class,
//            parentColumns = arrayOf("crn"),
//            childColumns = arrayOf("crn")
//        )
//    )//, indices = {@Index("crn")}
//)
//data class SavedCourse(
//    @PrimaryKey(autoGenerate = true)
//    var id: Int,
//    @ColumnInfo(name = "crn", index = true)
//    var crn: Int
//)

@Entity(tableName = "saved_course_table", indices = [
    Index(value = arrayOf("remoteId"), unique = true)
])
class SavedCourse(var remoteId: Int
//           var username: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localId")
    var id: Int = 0
}