package com.ninjatech.classroomfinder.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


/**
 * Class for displaying the relationship between a Section, and Course.
 */
@Entity(tableName = "section_course_join")
class SectionCourseJoin (
//    @Embedded(prefix = "course")
//    var section: Section? = Section(-1,"", -1),
//    @Relation(parentColumn = "id", entityColumn = "courseId", entity = Time::class)
//    var time: List<Time>? = null

//    @Relation(parentColumn = "id", entityColumn = "crn")
//    var sections: List<Section>? = null

//    @Embedded(prefix = "section_")
//    val section: Section
    @Embedded
    var course: Course? = null,
//    @Relation(parentColumn = "saveId", entityColumn = "remoteId",     entity = SavedCourse::class)
//    var saved: List<SavedCourse>? = null,
    @Relation(parentColumn = "id", entityColumn = "courseId", entity = Section::class)
    var time: List<TimeSectionJoin>? = null
)