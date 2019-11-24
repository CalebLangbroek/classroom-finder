package com.ninjatech.classroomfinder.map

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.ninjatech.classroomfinder.database.Coordinate
import com.ninjatech.classroomfinder.database.CoordinateAndReachable
import com.ninjatech.classroomfinder.database.CoordinateDao
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashSet

class MapViewModel(
    private val database: CoordinateDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var location = LocationLiveData(application)
    private var path: List<LatLng> = emptyList()

    fun getCurrentLocation() = location
    fun getPath() = path
    fun setPath(startId: String, destinationId: String) {
        coroutineScope.launch {
            val start: Coordinate = getCoordinate(startId)!!
            val destination: Coordinate = getCoordinate(destinationId)!!
            pathToDestination(start, destination)
        }
    }

    suspend fun getCoordinate(id: String): Coordinate? {
        val coordinate: Deferred<Coordinate?> = coroutineScope.async {
            getCoordinateById(id)
        }
        return coordinate.await()
    }

    suspend fun getReachable(id: String): List<CoordinateAndReachable>? {
        val reachable: Deferred<List<CoordinateAndReachable>?> = coroutineScope.async {
            getReachableCoordinateById(id)
        }
        return reachable.await()
    }

    private suspend fun getCoordinateById(id: String): Coordinate? {
        return withContext(Dispatchers.IO) {
            val coordinate = database.getCoordinateById(id)
            coordinate
        }
    }

    private suspend fun getReachableCoordinateById(id: String): List<CoordinateAndReachable>? {
        return withContext(Dispatchers.IO) {
            val coordinate = database.getCoordinateReachableById(id)
            coordinate
        }
    }

    private suspend fun pathToDestination(start: Coordinate, destination: Coordinate) {
        return withContext(Dispatchers.IO) {
            val fromCoordinate = mutableMapOf<Coordinate, Coordinate>()
            val cost = mutableMapOf<Coordinate, Double>()
            val pathCandidates =  PriorityQueue<CoordinateRank>()
            val examined = HashSet<Coordinate>()

            pathCandidates.add(start priority 0.0)
            fromCoordinate[start] = start
            cost[start] = 0.0

            while (pathCandidates.any()) {
                val current: Coordinate = pathCandidates.poll().coordinate

                if (current == destination) {
                    break;
                }

                examined.add(current)
                val nextCoordinates = getReachable(current.id)

                nextCoordinates!!.forEach {
                    val next: Coordinate = getCoordinate(it.reachableCoordinate.toCoorId)!!
                    val newCost = cost[current]?.plus(it.reachableCoordinate.cost)!!

                    if (!cost.containsKey(next) || newCost < cost[next]!!) {
                        cost[next] = newCost
                        val heuristicAndCost = newCost + estimateHeuristic(next, destination)
                        pathCandidates.add(next priority heuristicAndCost)
                        fromCoordinate[next] = current
                    }
                }
            }
            val latLngList = mutableListOf<LatLng>()
            var end = destination
            latLngList.add(LatLng(end.latitude, end.longitude))

            while (true) {
                if (end == start) {
                    break
                }
                val next = fromCoordinate[end]!!
                latLngList.add(LatLng(next.latitude, next.longitude))
                end = next
            }
            path = latLngList
        }
    }

    private fun estimateHeuristic(coordinate1: Coordinate, coordinate2: Coordinate): Double {
        val latitude = coordinate1.latitude - coordinate2.latitude
        val longitude = coordinate1.longitude - coordinate2.longitude
        return kotlin.math.sqrt((latitude * latitude) + (longitude * longitude));
    }

    private infix fun Coordinate.priority(priority: Double) =
        CoordinateRank(priority, this)
}

data class CoordinateRank(val priority: Double, val coordinate: Coordinate) : Comparable<CoordinateRank> {
    override fun compareTo(other: CoordinateRank) = when {
        priority < other.priority -> -1
        priority > other.priority -> 1
        else -> 0
    }
}



