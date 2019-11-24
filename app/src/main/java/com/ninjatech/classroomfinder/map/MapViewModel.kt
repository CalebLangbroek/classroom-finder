package com.ninjatech.classroomfinder.map

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.ninjatech.classroomfinder.R
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
    private var floor = 2
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

    fun getPath() = path

    fun setPath(startId: String, destinationId: String) {
        coroutineScope.launch {
            val start: Coordinate = getCoordinate(startId)!!
            val destination: Coordinate = getCoordinate(destinationId)!!
            pathToDestination(start, destination)
        }
    }

    fun getFloorSet(): MutableSet<FloorDetail?> {
        return when (floor) {
            1 -> mutableSetOf(floorDetails["B1"], floorDetails["D1"], floorDetails["A2"])
            2 -> mutableSetOf(floorDetails["B2"], floorDetails["D2"], floorDetails["A2"])
            3 -> mutableSetOf(floorDetails["B2"], floorDetails["D2"], floorDetails["A3"])
            else -> mutableSetOf(floorDetails["B2"], floorDetails["D2"], floorDetails["A2"])
        }
    }

    fun setFloorSet(floorNumber: Int) {
        floor = floorNumber
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
            val pathCandidates = PriorityQueue<CoordinateRank>()
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



