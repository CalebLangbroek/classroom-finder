package com.ninjatech.classroomfinder.database

import androidx.room.Embedded

/**
 * Class for displaying the relationship between a Section, Course, Time, and Room.
 */
class SectionAndCourse {
    @Embedded(prefix = "section_")
    lateinit var section: Section

    @Embedded(prefix = "course_")
    lateinit var course: Course

    @Embedded(prefix = "time_")
    lateinit var time : Time

    @Embedded(prefix = "room_")
    lateinit var room : ClassRoom
}
