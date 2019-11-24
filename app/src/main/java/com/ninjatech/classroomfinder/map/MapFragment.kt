package com.ninjatech.classroomfinder.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.database.AppDatabase
import com.ninjatech.classroomfinder.database.Coordinate
import com.ninjatech.classroomfinder.databinding.FragmentMapBinding
import java.util.*
import kotlin.collections.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var floorDetails: MutableMap<String, FloorDetail>
    private lateinit var binding: FragmentMapBinding

    private val defaultLocation = LatLng(49.028677, -122.284397)
    private val defaultZoom = 19.0f
    private var userLocation: LatLng = defaultLocation
    private var groundOverlayObjects = arrayOfNulls<GroundOverlay>(5)
    private var polylineOptions: PolylineOptions = PolylineOptions()
    private var polyline: Polyline? = null
    private var path: List<LatLng> = emptyList()

    companion object {
        private const val LOCATION_PERMISSION_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_map,
            container,
            false
        )

        // Initialize the View Model.
        this.initViewModel()

        // Create the path buttons.
        val drawPathButton: Button = binding.drawPathButton
        drawPathButton.setOnClickListener {
            drawPath()
        }
        val clearPathButton: Button = binding.clearPathButton
        clearPathButton.setOnClickListener {
            clearPath()
        }

        // Create the map.
        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        locationPermissionAction()
    }

    /**
     * Specifies the the actions taken when the map is loaded.
     */
    override fun onMapReady(gMap: GoogleMap?) {
        googleMap = gMap!!

        plotDefaultMaps()

        changeFloor("A333")

        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("UFV Abbotsford"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, defaultZoom))
    }

    private fun locationPermissionAction() {
        if (permissionGiven()!!) {
            startLocationUpdate()
        } else {
            activity?.run {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun startLocationUpdate() {
        binding.mapViewModel!!.getCurrentLocation().observe(this, Observer {
            userLocation = LatLng(it.latitude, it.longitude)
        })
    }

    private fun permissionGiven() = (activity?.run {
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    })

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            permissionGiven()
        }
    }

    private fun drawPath() {
        binding.mapViewModel!!.setPath("AH322", "AH304")

        path = binding.mapViewModel!!.getPath()!!

        if (path.isNotEmpty()) {
            polylineOptions.addAll(path)
            polyline = googleMap.addPolyline(polylineOptions)
        }
    }

    private fun clearPath() {
        if (polyline != null) {
            polyline = null
        }
    }

    private fun plotDefaultMaps() {
        val defaultFloors = mutableSetOf("A2", "B2", "C1", "D1", "K1")

        floorDetails = mutableMapOf(
            "K0" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.k0),
                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
            ),
            "K1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.k1),
                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
                // zIndex?
            ),
            "B2" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.b2),
                positionFromBounds = LatLngBounds(LatLng(49.030017, -122.286082), LatLng(49.030570, -122.285595))
            ),
            "A2" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.a2),
                positionFromBounds = LatLngBounds(LatLng(49.028965, -122.285190), LatLng(49.029486, -122.282777))
            ),
            "A3" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.a3),
                positionFromBounds = LatLngBounds(LatLng(49.028965, -122.285190), LatLng(49.029486, -122.282777))
            ),
            "A4" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.a4),
                positionFromBounds = LatLngBounds(LatLng(49.028965, -122.285190), LatLng(49.029486, -122.282777))
            ),
            "B1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.b1),
                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
            ),
            "B2" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.b2),
                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
            ),
            "B3" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.b3),
                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
            ),
            "B4" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.b4),
                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
            ),
            "C1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.c1),
                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
            ),
            "C2" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.c2),
                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
            ),
            "D1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.d1),
                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
            ),
            "D2" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.d2),
                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
            ),
            "D3" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.d3),
                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
            )
        )

        // Overlays only the default floors
        val it: Iterator<String> = defaultFloors.asIterable().iterator()
        var index = 0
        while (it.hasNext()) {
            val e = it.next()
            with(floorDetails.getValue(e)) {
                groundOverlayObjects[index] = googleMap.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(image)
                        .positionFromBounds(positionFromBounds)
                )
            }
            index += 1
        }
    }

    private fun changeFloor(classroom: String) {
        val letToNum = mapOf("A" to 0, "B" to 1, "C" to 2, "D" to 3, "K" to 4)
        val building: String = classroom[0].toString().toUpperCase()
        val floor: String = classroom[1].toString()
        val floorPlanNo: String = building + floor

        groundOverlayObjects[letToNum.getValue(building)]?.remove()

        with(floorDetails.getValue(floorPlanNo)) {
            groundOverlayObjects[letToNum.getValue(building)] = googleMap.addGroundOverlay(
                GroundOverlayOptions()
                    .image(image)
                    .positionFromBounds(positionFromBounds)
            )
        }

    }

    class FloorDetail(
        val image: BitmapDescriptor,
        val positionFromBounds: LatLngBounds
    )

    private fun initViewModel() {
        // Get this application
        val application = (this.activity)!!.application

        // Get the database
        val database = AppDatabase.getDatabase(application).coordinateDao

        // Create the ViewModel through the ViewModel factory
        val viewModelFactory = MapViewModelFactory(database, application)
        val mapViewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)

        // Bind to it
        this.binding.mapViewModel = mapViewModel
    }
}