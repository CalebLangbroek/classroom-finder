package com.ninjatech.classroomfinder

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.Manifest
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.ninjatech.classroomfinder.databinding.FragmentMapBinding

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var location: Location

    companion object {
        private const val LOCATION_PERMISSION_CODE = 101
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentMapBinding>(inflater, R.layout.fragment_map, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    private fun requestPermission(permissionType: String, requestCode: Int) {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    val alert = AlertDialog.Builder(activity)
                    alert.setTitle(R.string.permissionTitle)
                    alert.setMessage(R.string.permissionDialog)
                    val permissionAlert: AlertDialog = alert.create()
                    permissionAlert.show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        val permission =
            activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
        if (permission == PackageManager.PERMISSION_GRANTED) {
            mMap?.isMyLocationEnabled = true
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE)
        }

        // Plots an image onto Building B coordinates and rotates.
        //TODO Correctly place maps + add more
        val b1 = BitmapDescriptorFactory.fromResource(R.drawable.b1)
        val b2 = BitmapDescriptorFactory.fromResource(R.drawable.b2)

        val buildB = GroundOverlayOptions()
            .image(b2)
            //.anchor(0f, 0f)
            .bearing(-45f)
            .position(LatLng(49.028822, -122.285751), 110f, 50f)

        mMap.addGroundOverlay(buildB)

//        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        // Add marker at UFV Abbotsford campus location
        val ufvAbbotsfordCampus = LatLng(49.028677, -122.284397)
        mMap.addMarker(MarkerOptions().position(ufvAbbotsfordCampus).title("UFV Abbotsford"))
//        mMap!!.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(new LatLng (location!!.latitude, location!!.longitude),
//            20.0
//        ))
        mMap.moveCamera(CameraUpdateFactory.newLatLng((ufvAbbotsfordCampus)))
    }

}
