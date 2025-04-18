package com.example.nournet.fragments.donate

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.navArgs
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
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.R
import com.example.nournet.databinding.FragmentDonorLocationBinding

@AndroidEntryPoint
class DonorLocationFragment : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private lateinit var binding : FragmentDonorLocationBinding
    private val args: DonorLocationFragmentArgs by navArgs()
    private val requestCode = 1
    private lateinit var mMap : GoogleMap
    private lateinit var mGoogleAPIClient : GoogleApiClient
    private lateinit var mLastLocation : Location
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var auth : FirebaseAuth
    private lateinit var userID : String
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentDonorLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMaps) as? SupportMapFragment
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapFragment?.getMapAsync(this)
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

    override fun onConnected(p0 : Bundle?) {
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
        val latLng = LatLng(p0.latitude, p0.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Your Location")

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
        mMap.addMarker(markerOptions)!!.showInfoWindow()

        val location = args.donation

        val latLng2 = location.location?.let {
            location.location?.longitude?.let { it1 ->
                LatLng(
                    it.latitude,
                    it1
                )
            }
        }
        val markerOptions2 = latLng2?.let {
            MarkerOptions().position(it).title("Donor").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
        }
        latLng2?.let { CameraUpdateFactory.newLatLngZoom(it, 16F) }?.let { mMap.animateCamera(it) }
        markerOptions2?.let { mMap.addMarker(it) }!!.showInfoWindow()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode : Int,
        permissions : Array<String?>,
        grantResults : IntArray
    ) {
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mapFragment.getMapAsync(this)
            } else {
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}