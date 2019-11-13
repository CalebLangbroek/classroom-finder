package com.ninjatech.classroomfinder


import android.content.pm.PackageManager
import android.location.Location
import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition
import com.google.android.gms.maps.model.*

import com.ninjatech.classroomfinder.databinding.FragmentMapBinding


class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var cameraPosition: CameraPosition

    private var lastKnownLocation: Location? = null
    private var keyCameraPosition = "camera_position"
    private var keyLocation = "location"

    private val defaultLocation = LatLng(49.028677, -122.284397)
    private val defaultZoom = 17.0f

    companion object {
        private const val LOCATION_PERMISSION_CODE = 101
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(keyLocation)!!
            cameraPosition = savedInstanceState.getParcelable(keyCameraPosition)!!
        }

        if (!permissionGiven()) {
            getLocationPermission()
        }

        val binding = DataBindingUtil.inflate<FragmentMapBinding>(inflater, R.layout.fragment_map, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (googleMap != null) {
            outState.putParcelable(keyCameraPosition, googleMap.cameraPosition)
            outState.putParcelable(keyLocation, lastKnownLocation)
            super.onSaveInstanceState(outState)
        }
    }

    override fun onMapReady(gMap: GoogleMap?) {
        googleMap = gMap!!

        if(permissionGiven()) {
            getDeviceLocation()
        }

        plotMaps()

        // Add marker at UFV Abbotsford campus location
        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("UFV Abbotsford"))

        pointToPosition(defaultLocation)
    }

    private fun pointToPosition(position: LatLng){
        val cameraPosition = CameraPosition.builder()
            .target(position)
            .zoom(17f).build()
        googleMap.animateCamera(newCameraPosition(cameraPosition))
    }
    private fun plotMaps(){
        // Plots an image onto Building B coordinates and rotates.
        val floorDetails = mutableMapOf(
            "k1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.k1),
                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
                // zIndex?
            ),
            "k0" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.k0),
                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
            )
        )

        // Iterates through the floorDetailsMap and prints values
        floorDetails.keys.map{
            with (floorDetails.getValue(it)){
                googleMap.addGroundOverlay(GroundOverlayOptions()
                    .image(image)
                    .positionFromBounds(positionFromBounds)
                )
            }
        }
    }

    private fun getDeviceLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener() { task ->
            lastKnownLocation = if (task.isSuccessful && task.result != null) {
                task.result!!
            } else {
                defaultLocation as Location
            }
        }
    }


    private fun permissionGiven(): Boolean {
        return (checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun getLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
    }

    class FloorDetail(
        val image: BitmapDescriptor,
        val positionFromBounds: LatLngBounds
        //val zIndex: Float
    )

}