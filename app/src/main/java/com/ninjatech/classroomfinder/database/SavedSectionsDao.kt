package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

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
     *
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


    @Query("DELETE FROM saved_sections")
    fun deleteAll()
}