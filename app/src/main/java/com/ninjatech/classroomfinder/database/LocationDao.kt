package com.ninjatech.classroomfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationDao {

    @Insert
    fun insert(location: Location)

    @Update
    fun update(location: Location)

    @Delete
    fun delete(location: Location)

    @Query("SELECT * from location_table WHERE id = :key")
    fun get(key: Int): Location?

    @Query("DELETE FROM location_table")
    fun deleteAll()
}