package com.ninjatech.classroomfinder.database

import android.app.LauncherActivity
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity(tableName = "time_section_course_join")
class TimeSectionJoin {
//    @Embedded(prefix = "course")
//    var course: Course? = null
//
//    @Relation(parentColumn = "id", entityColumn = "courseId", entity = Section::class)
//    var section: List<Section>? = null
//
//    @Relation(parentColumn = "crn", entityColumn = "id", entity = Section::class)
//    var time: List<SectionCourseJoin>? = null
    @Embedded
    var list: Section? = null

    @Relation(parentColumn = "remoteId", entityColumn = "sectionId", entity = Time::class)
    var time: List<Time>? = null

}