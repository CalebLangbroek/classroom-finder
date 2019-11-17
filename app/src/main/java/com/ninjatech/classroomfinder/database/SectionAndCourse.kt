package com.ninjatech.classroomfinder.database

import androidx.room.Embedded

/**
 * Class for displaying the relationship between a Section, and Course.
 */
class SectionAndCourse {
    @Embedded
    lateinit var section: Section

    @Embedded
    lateinit var course: Course
}
