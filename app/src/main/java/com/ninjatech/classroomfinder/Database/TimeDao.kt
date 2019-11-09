package com.ninjatech.classroomfinder

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface TimeDao {

    @Query("SELECT * from time_table ORDER BY crn ASC")
    fun getAlphabetizedTimes(): LiveData<List<TimeTable>>

    @Query("SELECT * FROM section WHERE crn IN (:crn)")
    fun loadAllByIds(crn: Array<Int>): List<Section>

    @Query("SELECT * FROM time_table")
    fun getAllTimes(): List<TimeTable>

    @Query("SELECT * FROM time_table WHERE id=:id")
    fun findTimesForCourse(id: Int): List<TimeTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTime(vararg entity: TimeTable)

    @Insert
    fun insert(timeTable: TimeTable)

    @Query("DELETE FROM time_table")
    suspend fun deleteAll()
}