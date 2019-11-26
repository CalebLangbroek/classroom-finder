package com.ninjatech.classroomfinder.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.database.Coordinate
import com.ninjatech.classroomfinder.databinding.FragmentMapBinding
import kotlin.collections.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var binding: FragmentMapBinding

    private val defaultLocation = LatLng(49.028677, -122.284397)
    private val defaultZoom = 18.0f
    private var userLocation: LatLng = defaultLocation
    private var groundOverlayObjects = mutableSetOf<GroundOverlay>()
    private var polylineOptions: PolylineOptions = PolylineOptions()
    private var polyline: Polyline? = null
    private var path: List<Coordinate> = emptyList()
    private var pathDrawn = false

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

        // Get the coordinate id if navigated to from the profile
        val arguments = MapFragmentArgs.fromBundle(arguments!!)

        // Coordinate id
        binding.destinationId = arguments.coordinateId

        // Initialize the View Model.
        this.initViewModel()

        this.initButtons()

        locationPermissionAction()

        // Create the map.
        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    /**
     * Specifies the the actions taken when the map is loaded.
     */
    override fun onMapReady(gMap: GoogleMap?) {
        googleMap = gMap!!

        googleMap.isMyLocationEnabled = true

        if (binding.destinationId != "null") {
            when (binding.destinationId!!.elementAt(2)) {
                '1' -> binding.mapViewModel!!.setFloorSet(1)
                '2' -> binding.mapViewModel!!.setFloorSet(2)
                '3' -> binding.mapViewModel!!.setFloorSet(3)
                else -> binding.mapViewModel!!.setFloorSet(2)
            }
        }

        plotBuildingLayouts(binding.mapViewModel!!.getFloorSet())

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
            if (binding.destinationId != "null") {
                setPath()
            }
            if (!pathDrawn) {
                Handler().postDelayed({
                    drawPath()
                }, 2000)
            }
            pathDrawn = true
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

    private fun setPath() {
        val currentLocation = Coordinate("id", userLocation.latitude, userLocation.longitude)
        binding.mapViewModel!!.setPath(
            currentLocation,
            binding.destinationId!!
        )
    }

    private fun drawPath() {
        val latLngPath = mutableListOf<LatLng>()
        path = binding.mapViewModel!!.getPath()
        path.forEach {
            latLngPath.add(LatLng(it.latitude, it.longitude))
        }
        if (latLngPath.isNotEmpty()) {
            polylineOptions.addAll(latLngPath)
            polyline = googleMap.addPolyline(polylineOptions)
        }
    }

    private fun clearPath() {
        polyline?.remove()
        polyline = null
    }

    private fun plotBuildingLayouts(floorDetails: MutableSet<FloorDetail?>) {
        groundOverlayObjects.forEach {
            it.remove()
        }
        groundOverlayObjects.clear()
        floorDetails.forEach {
            groundOverlayObjects.add(
                googleMap.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(it!!.image)
                        .positionFromBounds(it.positionFromBounds)
                )
            )
        }
    }

    private fun initButtons() {
        val clearPathButton: Button = binding.clearPathButton
        clearPathButton.setOnClickListener {
            clearPath()
        }
        val changeToFloor1Button: Button = binding.changeToFloor1Button
        changeToFloor1Button.setOnClickListener {
            binding.mapViewModel!!.setFloorSet(1)
            plotBuildingLayouts(binding.mapViewModel!!.getFloorSet())
        }
        val changeToFloor2Button: Button = binding.changeToFloor2Button
        changeToFloor2Button.setOnClickListener {
            binding.mapViewModel!!.setFloorSet(2)
            plotBuildingLayouts(binding.mapViewModel!!.getFloorSet())
        }
        val changeToFloor3Button: Button = binding.changeToFloor3Button
        changeToFloor3Button.setOnClickListener {
            binding.mapViewModel!!.setFloorSet(3)
            plotBuildingLayouts(binding.mapViewModel!!.getFloorSet())
        }
    }

    private fun initViewModel() {
        // Get this application
        val application = (this.activity)!!.application

        // Create the ViewModel through the ViewModel factory
        val mapViewModel = ViewModelProviders.of(this, SavedStateViewModelFactory(application, this))
            .get(MapViewModel::class.java)

        // Bind to it
        this.binding.mapViewModel = mapViewModel
    }
}