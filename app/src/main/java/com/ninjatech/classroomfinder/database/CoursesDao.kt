package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CoursesDao {
    @Query(
        """
        SELECT sections.crn AS section_crn, sections.course_id AS section_course_id, sections.title AS section_title,
        courses.id AS course_id, courses.title AS course_title, courses.subject AS course_subject,
        times.id AS time_id, times.section_crn AS time_section_crn, times.room_id AS time_room_id, times.day AS time_day, times.start_time AS time_start_time, times.end_time AS time_end_time, times.start_date AS time_start_date, times.end_date AS time_end_date,
        rooms.id AS room_id, rooms.coor_id AS room_coor_id, rooms.room AS room_room, rooms.building AS room_building, rooms.level AS room_level
        FROM sections INNER JOIN courses ON sections.course_id = courses.id 
        INNER JOIN times ON sections.crn = times.section_crn
        INNER JOIN rooms ON times.room_id = rooms.id
        ORDER BY courses.title ASC
    """
    )
    fun getAllCourses(): LiveData<List<SectionAndCourse>>?

    /**
     * Query the database for a match on the title or subject.
     * Using the % operator so it can match on either side.
     */
    @Query(
        """
        SELECT sections.crn AS section_crn, sections.course_id AS section_course_id, sections.title AS section_title,
        courses.id AS course_id, courses.title AS course_title, courses.subject AS course_subject,
        times.id AS time_id, times.section_crn AS time_section_crn, times.room_id AS time_room_id, times.day AS time_day, times.start_time AS time_start_time, times.end_time AS time_end_time, times.start_date AS time_start_date, times.end_date AS time_end_date,
        rooms.id AS room_id, rooms.coor_id AS room_coor_id, rooms.room AS room_room, rooms.building AS room_building, rooms.level AS room_level
        FROM sections INNER JOIN courses ON sections.course_id = courses.id 
        INNER JOIN times ON sections.crn = times.section_crn
        INNER JOIN rooms ON times.room_id = rooms.id
        WHERE courses.title LIKE '%' || :query || '%' OR courses.subject LIKE '%' || :query || '%'
        ORDER BY courses.title ASC
    """
    )
    fun getCourseByTitleOrSubject(query: String): LiveData<List<SectionAndCourse>>?
}