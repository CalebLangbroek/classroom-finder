package com.ninjatech.classroomfinder.database

import androidx.room.Embedded

/**
 * Class for displaying the relationship between a Section, and Course.
 */
data class SectionAndCourse(
    @Embedded(prefix = "course_")
    val course: Course,

    @Embedded(prefix = "section_")
    val section: Section
)
