package com.ninjatech.classroomfinder.database

import androidx.room.Embedded

class CoordinateAndReachable{
    @Embedded
    lateinit var coordinate: Coordinate

    @Embedded(prefix = "reachable_")
    lateinit var reachableCoordinate : ReachableCoordinate
}