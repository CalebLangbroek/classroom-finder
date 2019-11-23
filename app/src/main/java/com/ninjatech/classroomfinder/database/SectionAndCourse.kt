package com.ninjatech.classroomfinder.database

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Class for displaying the relationship between a Section, Course, Time, and Room.
 */
class SectionAndCourse {

    @Embedded(prefix = "section_")
    lateinit var section: Section

    @Embedded(prefix = "course_")
    lateinit var course: Course

    @Embedded(prefix = "saved_")
    lateinit var savedSection: SavedSection

    @Relation(parentColumn = "section_crn", entityColumn = "section_crn")
    var timesAndRooms : List<TimeAndClassRoom> = emptyList()
}
