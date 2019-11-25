package com.ninjatech.classroomfinder.map

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.database.AppDatabase
import com.ninjatech.classroomfinder.database.Coordinate
import com.ninjatech.classroomfinder.database.CoordinateAndReachable
import com.ninjatech.classroomfinder.database.CoordinateDao
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashSet

class MapViewModel(
    application: Application,
    state: SavedStateHandle
) : AndroidViewModel(application) {

    companion object {
        private const val FLOOR_KEY = "floor"
        private const val PATH_KEY = "path"
    }

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val database: CoordinateDao = AppDatabase.getDatabase(application).coordinateDao
    private val savedStateHandle = state

    private var location = LocationLiveData(application)

    private var floorDetails = mutableMapOf(
        "A2" to FloorDetail(
            image = BitmapDescriptorFactory.fromResource(R.drawable.a2),
            positionFromBounds = LatLngBounds(LatLng(49.028963, -122.285213), LatLng(49.029499, -122.282799))
        ),
        "A3" to FloorDetail(
            image = BitmapDescriptorFactory.fromResource(R.drawable.a3),
            positionFromBounds = LatLngBounds(LatLng(49.028962, -122.285215), LatLng(49.029500, -122.282791))
        ),
//            "A4" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.a4),
//                positionFromBounds = LatLngBounds(LatLng(49.028965, -122.285190), LatLng(49.029486, -122.282777))
//            ),
        "B1" to FloorDetail(
            image = BitmapDescriptorFactory.fromResource(R.drawable.b1),
            positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286516), LatLng(49.029342, -122.285084))
        ),
        "B2" to FloorDetail(
            image = BitmapDescriptorFactory.fromResource(R.drawable.b2),
            positionFromBounds = LatLngBounds(LatLng(49.028421, -122.286517), LatLng(49.029343, -122.285087))
        ),
//            "B3" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.b3),
//                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
//            ),
//            "B4" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.b4),
//                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
//            ),
//            "C1" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.c1),
//                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
//            ),
//            "C2" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.c2),
//                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
//            ),
        "D1" to FloorDetail(
            image = BitmapDescriptorFactory.fromResource(R.drawable.d1),
            positionFromBounds = LatLngBounds(LatLng(49.027785, -122.285911), LatLng(49.028601, -122.285143))
        ),
        "D2" to FloorDetail(
            image = BitmapDescriptorFactory.fromResource(R.drawable.d2),
            positionFromBounds = LatLngBounds(LatLng(49.027779, -122.285918), LatLng(49.028604, -122.285135))
        )
//            "D3" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.d3),
//                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
//            ),
//            "K0" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.k0),
//                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
//            ),
//            "K1" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.k1),
//                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
//                // zIndex?
//            )
    )

    fun getCurrentLocation() = location

    fun getPath() = savedStateHandle.get<List<Coordinate>>(PATH_KEY) ?: emptyList()

    fun setPath(startLocation: Coordinate, destinationId: String) {
        coroutineScope.launch {
            val coordinateList = getCoordinateList()
            val destination: Coordinate = getCoordinate(destinationId)!!
            pathToDestination(findClosestCoordinate(coordinateList, startLocation), destination)
        }
    }

    fun getFloorSet(): MutableSet<FloorDetail?> {
        return when (savedStateHandle.get(FLOOR_KEY) ?: 2) {
            1 -> mutableSetOf(floorDetails["B1"], floorDetails["D1"], floorDetails["A2"])
            2 -> mutableSetOf(floorDetails["B2"], floorDetails["D2"], floorDetails["A2"])
            3 -> mutableSetOf(floorDetails["B2"], floorDetails["D2"], floorDetails["A3"])
            else -> mutableSetOf(floorDetails["B2"], floorDetails["D2"], floorDetails["A2"])
        }
    }

    fun setFloorSet(floorNumber: Int) = savedStateHandle.set(FLOOR_KEY, floorNumber)

    private suspend fun getCoordinate(id: String): Coordinate? {
        val coordinate: Deferred<Coordinate?> = coroutineScope.async {
            getCoordinateById(id)
        }
        return coordinate.await()
    }

    private suspend fun getReachable(id: String): List<CoordinateAndReachable>? {
        val reachable: Deferred<List<CoordinateAndReachable>?> = coroutineScope.async {
            getReachableCoordinateById(id)
        }
        return reachable.await()
    }

    private suspend fun getCoordinateList(): List<Coordinate>? {
        val coordinateList: Deferred<List<Coordinate>?> = coroutineScope.async {
            getAllCoordinates()
        }
        return coordinateList.await()
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

    private suspend fun getAllCoordinates(): List<Coordinate>? {
        return withContext(Dispatchers.IO) {
            val coordinateList = database.getAllCoordinates()
            coordinateList
        }
    }

    private suspend fun pathToDestination(start: Coordinate, destination: Coordinate) {
        return withContext(Dispatchers.IO) {
            val fromCoordinate = mutableMapOf<Coordinate, Coordinate>()
            val cost = mutableMapOf<Coordinate, Double>()
            val pathCandidates = PriorityQueue<CoordinateRank?>()
            val examined = HashSet<Coordinate>()

            pathCandidates.add(start priority 0.0)
            fromCoordinate[start] = start
            cost[start] = 0.0

            while (pathCandidates.any()) {
                val current: Coordinate = pathCandidates.poll()!!.coordinate

                if (current == destination) {
                    break
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
            val coordinateList = mutableListOf<Coordinate>()
            var end = destination
            coordinateList.add(end)

            while (true) {
                if (end == start) {
                    break
                }
                val next = fromCoordinate[end]!!
                coordinateList.add(next)
                end = next
            }
            createPath(coordinateList)
        }
    }

    private fun findClosestCoordinate(coordinateList: List<Coordinate>?, location: Coordinate): Coordinate {
        val closest = PriorityQueue<CoordinateRank>()
        coordinateList!!.forEach {
            val heuristic = estimateHeuristic(it, location)
            closest.add(it priority heuristic)
        }
        return closest.poll().coordinate
    }

    private fun createPath(path: List<Coordinate>) = savedStateHandle.set(PATH_KEY, path)

    private fun estimateHeuristic(coordinate1: Coordinate, coordinate2: Coordinate): Double {
        val latitude = coordinate1.latitude - coordinate2.latitude
        val longitude = coordinate1.longitude - coordinate2.longitude
        return kotlin.math.sqrt((latitude * latitude) + (longitude * longitude))
    }

    private infix fun Coordinate.priority(priority: Double) =
        CoordinateRank(priority, this)
}

class FloorDetail(
    val image: BitmapDescriptor,
    val positionFromBounds: LatLngBounds
)

data class CoordinateRank(val priority: Double, val coordinate: Coordinate) : Comparable<CoordinateRank> {
    override fun compareTo(other: CoordinateRank) = when {
        priority < other.priority -> -1
        priority > other.priority -> 1
        else -> 0
    }
}



