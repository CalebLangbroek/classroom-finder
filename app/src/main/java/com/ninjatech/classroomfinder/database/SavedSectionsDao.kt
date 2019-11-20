package com.ninjatech.classroomfinder.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SavedSectionsDao {

    @Insert
    fun insert(savedSection: SavedSection)

    @Delete
    fun delete(savedSection: SavedSection)

    @Query(
        """
        SELECT sections.crn AS section_crn, sections.course_id AS section_course_id, sections.title AS section_title,
        courses.id AS course_id, courses.title AS course_title, courses.subject AS course_subject,
        times.id AS time_id, times.section_crn AS time_section_crn, times.room_id AS time_room_id, times.day AS time_day, times.start_time AS time_start_time, times.end_time AS time_end_time, times.start_date AS time_start_date, times.end_date AS time_end_date,
        rooms.id AS room_id, rooms.coor_id AS room_coor_id, rooms.room AS room_room, rooms.building AS room_building, rooms.level AS room_level
        FROM sections INNER JOIN saved_sections ON saved_sections.section_crn = sections.crn
        INNER JOIN courses ON sections.course_id = courses.id 
        INNER JOIN times ON sections.crn = times.section_crn
        INNER JOIN rooms ON times.room_id = rooms.id
        ORDER BY courses.title ASC
    """
    )
    fun getAllSavedCourseData(): LiveData<List<SectionAndCourse>>?

    @Query("DELETE FROM saved_sections")
    fun deleteAll()

    @Query ("SELECT times.day, times.start_time FROM saved_sections INNER JOIN sections ON crn = saved_sections.section_crn INNER JOIN courses ON course_id = courses.id INNER JOIN times on crn = times.section_crn ORDER BY start_time ASC")
    fun getAlarms(): Cursor
}