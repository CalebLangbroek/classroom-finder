package com.ninjatech.classroomfinder.database

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

    @Query("DELETE FROM saved_sections")
    fun deleteAll()
}