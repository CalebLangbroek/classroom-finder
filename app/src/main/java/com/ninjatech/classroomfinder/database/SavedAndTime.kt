package com.ninjatech.classroomfinder.database

import androidx.room.Embedded

class SavedAndTime {

    @Embedded(prefix = "section_")
    lateinit var section: Section

    @Embedded(prefix = "course_")
    lateinit var course: Course

    @Embedded(prefix = "saved_")
    lateinit var savedSection: SavedSection

//    @Relation(parentColumn = "section_crn", entityColumn = "section_crn")
    @Embedded(prefix = "time_")
    lateinit var times: Time
}