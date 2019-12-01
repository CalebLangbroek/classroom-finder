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
        saved_sections.id AS saved_id, saved_sections.section_crn AS saved_section_crn
        FROM sections INNER JOIN saved_sections ON saved_sections.section_crn = sections.crn
        INNER JOIN courses ON sections.course_id = courses.id
        ORDER BY courses.title ASC
    """
    )
    fun getAllSavedCourseData(): LiveData<List<SectionAndCourse>>?

    /**
     * Get a coordinate for section.
     */
    @Query(
        """
        SELECT coordinates.*
        FROM sections INNER JOIN times ON sections.crn = times.section_crn
        INNER JOIN rooms ON times.room_id = rooms.id
        INNER JOIN coordinates ON rooms.coor_id = coordinates.id
        WHERE sections.crn = :crn
        LIMIT 1
    """
    )
    fun getCoordinateFromCrn(crn: Int): Coordinate?

    /**
     * Get time of the next class for the current day.
     */
    @Transaction@Query ("""
            SELECT sections.crn AS section_crn, sections.course_id AS section_course_id, sections.title AS section_title,
            courses.id AS course_id, courses.title AS course_title, courses.subject AS course_subject,
            saved_sections.id AS saved_id, saved_sections.section_crn AS saved_section_crn, times.id AS time_id,
            times.room_id AS time_room_id, times.day AS time_day, times.start_time AS time_start_time,
            times.end_time AS time_end_time , times.start_date AS time_start_date, times.end_date AS time_end_date,
            times.section_crn AS time_section_crn
            FROM sections INNER JOIN saved_sections ON saved_sections.section_crn = sections.crn
            INNER JOIN courses ON sections.course_id = courses.id
            INNER JOIN times on sections.crn = times.section_crn
            WHERE day LIKE :date AND start_time > :time
			ORDER BY start_time ASC
            LIMIT 1
            """
    )
    fun getAlarms(date: String, time : String): SavedAndTime

    /**
     * Check the number of rows in the getAlarms query
     */
    @Transaction@Query ("""
            SELECT count(start_time)
            FROM saved_sections
            INNER JOIN sections on saved_sections.section_crn = sections.crn
            INNER JOIN times on crn = times.section_crn
            WHERE times.day LIKE :date AND times.start_time > :time
			ORDER BY times.start_time ASC
            LIMIT 1
            """
    )
    fun getAlarmsCount(date: String, time : String): Int

    @Query("DELETE FROM saved_sections WHERE section_crn = :crn")
    fun deleteSavedSectionByCrn(crn: Int)

    @Query("DELETE FROM saved_sections")
    fun deleteAll()
}