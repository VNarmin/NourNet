package com.example.nournet.fragments.foodmap

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.R
import com.example.nournet.databinding.FragmentFoodMapBinding
import javax.inject.Inject


@AndroidEntryPoint
class FoodMapFragment : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private lateinit var binding : FragmentFoodMapBinding
    private lateinit var mMap : GoogleMap
    private lateinit var mGoogleAPIClient : GoogleApiClient
    private lateinit var mLastLocation : Location
    private lateinit var mLocationRequest : LocationRequest
    private val requestCode = 11
    private lateinit var mapFragment : SupportMapFragment
    @Inject lateinit var auth : FirebaseAuth
    private lateinit var userID : String
    @Inject lateinit var database : FirebaseFirestore

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentFoodMapBinding.inflate(inflater, container, false)
        val view = binding.root
        userID = auth.currentUser!!.uid

        mapFragment = childFragmentManager.findFragmentById(R.id.googleMaps) as SupportMapFragment
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapFragment.getMapAsync(this)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
        }

        return view
    }

    @Synchronized
    fun buildGoogleAPIClient() {
        mGoogleAPIClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleAPIClient.connect()
    }

    override fun onMapReady(p0 : GoogleMap) {
        mMap = p0

        buildGoogleAPIClient()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }

    override fun onConnected(bundle : Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleAPIClient,
            mLocationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0 : Int) { }

    override fun onConnectionFailed(p0 : ConnectionResult) { }

    override fun onLocationChanged(p0 : Location) {
        mLastLocation = p0
        showLocation()
        val latLng = LatLng(p0.latitude, p0.longitude)

        val markerOptions1 = MarkerOptions().position(latLng).title("Your Location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
        mMap.addMarker(markerOptions1)?.showInfoWindow()
    }

    private fun showLocation() {
        database.collection("donations")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        if (document.contains("location") && document.contains("name") &&
                            document.contains("description")
                        ) {
                            val location = document["location"] as GeoPoint?
                            val title = document["name"] as String?
                            val description = document["description"] as String?
                            val latLng = LatLng(
                                location!!.latitude, location.longitude
                            )
                            mMap.addMarker(
                                MarkerOptions().position(latLng).title("$title Donor")
                                    .snippet(description).icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                    )
                            )
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode : Int,
        permissions : Array<String?>,
        grantResults : IntArray
    ) {
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragment.getMapAsync(this)
            } else {
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}