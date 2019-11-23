package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CoursesDao {

    /**
     * Query to see if a section has been saved to the user's saved sections.
     */
    @Query("SELECT * FROM saved_sections WHERE section_crn = :crn")
    fun getSavedSectionByCrn(crn : Int) : SavedSection?

    @Delete
    fun deleteSavedSection(savedSection : SavedSection) : Int

    @Insert
    fun insertSavedSection(savedSection: SavedSection) : Long

    @Query(
        """
        SELECT sections.crn AS section_crn, sections.course_id AS section_course_id, sections.title AS section_title,
        courses.id AS course_id, courses.title AS course_title, courses.subject AS course_subject,
        ifnull(saved_sections.id, -1)  AS saved_id, ifnull(saved_sections.section_crn, -1) AS saved_section_crn
        FROM sections LEFT JOIN saved_sections ON saved_sections.section_crn = sections.crn
        INNER JOIN courses ON sections.course_id = courses.id
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
        ifnull(saved_sections.id, -1)  AS saved_id, ifnull(saved_sections.section_crn, -1) AS saved_section_crn
        FROM sections LEFT JOIN saved_sections ON saved_sections.section_crn = sections.crn
        INNER JOIN courses ON sections.course_id = courses.id
        WHERE courses.title LIKE '%' || :query || '%' OR courses.subject LIKE '%' || :query || '%'
        ORDER BY courses.title ASC
    """
    )
    fun getCourseByTitleOrSubject(query: String): LiveData<List<SectionAndCourse>>?
}