package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimeDao {

    @Query("SELECT * from time_table ORDER BY crn ASC")
    fun getAlphabetizedTimes(): LiveData<List<Time>>

    @Query("SELECT * FROM section_table WHERE crn IN (:crn)")
    fun loadAllByIds(crn: Array<Int>): List<Section>

    @Query("SELECT * FROM time_table")
    fun getAllTimes(): List<Time>

    @Query("SELECT * FROM time_table WHERE id=:id")
    fun findTimesForCourse(id: Int): List<Time>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTime(entity: Time)

    @Insert
    fun insert(Time: Time)

    @Query("DELETE FROM time_table")
    suspend fun deleteAll()
}