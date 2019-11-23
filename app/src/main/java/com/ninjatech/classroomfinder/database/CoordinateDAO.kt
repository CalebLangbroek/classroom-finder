package com.ninjatech.classroomfinder.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CoordinateDAO {

    /**
     * Get a coordinate from its id.
     */
    @Query("SELECT * FROM coordinates WHERE coordinates.id = :coorId")
    fun getCoordinateById(coorId : String) : Coordinate?

    /**
     * Get a list of coordinates that are reachable from a coordinate id.
     */
    @Query("""
       SELECT coordinates.*,
       reachable_coordinates.id AS reachable_id, reachable_coordinates.from_coor_id AS reachable_from_coor_id, reachable_coordinates.to_coor_id AS reachable_to_coor_id, reachable_coordinates.cost AS reachable_cost
       FROM coordinates INNER JOIN reachable_coordinates 
       WHERE reachable_coordinates.from_coor_id = :coorId AND reachable_coordinates.to_coor_id = coordinates.id 
    """)
    fun getCoordinateReachableById(coorId: String) : List<CoordinateAndReachable>?

    /**
     * Get a coordinate from a room id.
     */
    @Query("SELECT coordinates.* FROM coordinates INNER JOIN rooms WHERE rooms.id = :roomId AND rooms.coor_id = coordinates.id")
    fun getCoordinateByRoomId(roomId : String) : Coordinate?
}