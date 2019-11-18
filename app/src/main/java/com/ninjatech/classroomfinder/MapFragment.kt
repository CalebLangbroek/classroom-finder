package com.ninjatech.classroomfinder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.ninjatech.classroomfinder.databinding.FragmentMapBinding
import java.lang.Exception

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mapViewModel: MapViewModel

    private val defaultLocation = LatLng(49.028677, -122.284397)
    private val defaultZoom = 17.0f
    private var userLocation: LatLng = defaultLocation

    companion object {
        private const val LOCATION_PERMISSION_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = DataBindingUtil.inflate<FragmentMapBinding>(
            inflater,
            R.layout.fragment_map,
            container,
            false
        )

        mapViewModel = activity?.run {
            ViewModelProviders.of(this)[MapViewModel::class.java]
        } ?: throw Exception("Activity Not Found")

        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //locationPermissionAction()
    }

    override fun onMapReady(gMap: GoogleMap?) {
        googleMap = gMap!!

        plotMaps()

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
        mapViewModel.getCurrentLocation().observe(this, Observer {
            userLocation = LatLng(it.latitude, it.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, defaultZoom));
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

    private fun plotMaps() {
        // Plots an image onto Building B coordinates and rotates.
        val floorDetails = mutableMapOf(
//            "k0" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.k0),
//                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
//            ),
            "k1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.k1),
                positionFromBounds = LatLngBounds(LatLng(49.030379, -122.288909), LatLng(49.031230, -122.288296))
                // zIndex?
            ),
//            "b2" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.b2),
//                positionFromBounds = LatLngBounds(LatLng(49.030017, -122.286082), LatLng(49.030570, -122.285595))
//            ),
//            "a2" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.a2),
//                positionFromBounds = LatLngBounds(LatLng(49.028965, -122.285190), LatLng(49.029486, -122.282777))
//            ),
            "a3" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.a3),
                positionFromBounds = LatLngBounds(LatLng(49.028965, -122.285190), LatLng(49.029486, -122.282777))
            ),
//            "a4" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.a4),
//                positionFromBounds = LatLngBounds(LatLng(49.028965, -122.285190), LatLng(49.029486, -122.282777))
//            ),
            "b1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.b1),
                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
            ),
//            "b2" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.b2),
//                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
//            ),
//            "b3" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.b3),
//                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
//            ),
//            "b4" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.b4),
//                positionFromBounds = LatLngBounds(LatLng(49.028417, -122.286510), LatLng(49.029343, -122.285060))
//            ),
            "c1" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.c1),
                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
            ),
//            "c2" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.c2),
//                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
//            ),
//            "d1" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.d1),
//                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
//            ),
            "d2" to FloorDetail(
                image = BitmapDescriptorFactory.fromResource(R.drawable.d2),
                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
            )
//            "d3" to FloorDetail(
//                image = BitmapDescriptorFactory.fromResource(R.drawable.d3),
//                positionFromBounds = LatLngBounds(LatLng(49.027776, -122.287581), LatLng(49.028558, -122.286003))
//            )

        )

        // Iterates through the floorDetailsMap and prints values
        floorDetails.keys.map {
            with(floorDetails.getValue(it)) {
                googleMap.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(image)
                        .positionFromBounds(positionFromBounds)
                )
            }
        }
    }

    class FloorDetail(
        val image: BitmapDescriptor,
        val positionFromBounds: LatLngBounds
        //val zIndex: Float
    )

}