package com.bangkit.glowfyapp.view.home.fragments.clinic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.glowfyapp.BuildConfig.MAPS_API_KEY
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ClinicData
import com.bangkit.glowfyapp.databinding.FragmentClinicBinding
import com.bangkit.glowfyapp.utils.PermissionLocationUtils
import com.bangkit.glowfyapp.view.customview.CustomAlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClinicFragment : Fragment(), OnMapReadyCallback, ClinicAdapter.ClinicItemClickListener, LocationChangeReceiver.LocationChangeListener {

    private var _binding: FragmentClinicBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient

    private lateinit var locationChangeReceiver: LocationChangeReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Places.initialize(requireContext(), MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationChangeReceiver = LocationChangeReceiver(this)

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (::mMap.isInitialized) {
                mMap.clear()
                checkLocationAndLoad()
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        requireContext().registerReceiver(locationChangeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(locationChangeReceiver)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (!PermissionLocationUtils.checkLocationPermission(requireContext())) {
            PermissionLocationUtils.requestLocationPermission(requireActivity())
            return
        }
        checkLocationAndLoad()
    }

    private fun checkLocationAndLoad() {
        if (!PermissionLocationUtils.checkLocationPermission(requireContext())) {
            PermissionLocationUtils.requestLocationPermission(requireActivity())
            return
        }
        if (!isLocationEnabled()) {
            showLocationAlert()
            binding.swipeRefreshLayout.isRefreshing = false
        } else {
            mMap.isMyLocationEnabled = true
            loadCurrentLocation()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun loadCurrentLocation() {
        if (!PermissionLocationUtils.checkLocationPermission(requireContext())) {
            PermissionLocationUtils.requestLocationPermission(requireActivity())
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    Log.d("testlokasi", "Current location: $currentLatLng")
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    findNearbyClinics(currentLatLng)
                }
            }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showLocationAlert() {
        val alertDialog = CustomAlertDialog(requireContext(), R.raw.animation_location_need, R.string.locationNeed, R.string.turnLocation)
        alertDialog.setOnDismissListener {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        alertDialog.show()
    }

    private fun findNearbyClinics(location: LatLng) {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES, Place.Field.ADDRESS)
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                withContext(Dispatchers.Main) {
                    showLoading(false)
                }
                return@launch
            }

            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                CoroutineScope(Dispatchers.Main).launch {
                    showLoading(false)
                    if (task.isSuccessful) {
                        val response = task.result
                        val places = mutableListOf<ClinicData>()
                        for (placeLikelihood in response?.placeLikelihoods ?: emptyList()) {
                            val place = placeLikelihood.place
                            if (place.types?.contains(Place.Type.HEALTH) == true || place.types?.contains(
                                    Place.Type.BEAUTY_SALON) == true) {
                                place.latLng?.let {
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clinic_marker)
                                    val bitmap = drawable?.toBitmap()
                                    val icon = bitmap?.let { it1 -> BitmapDescriptorFactory.fromBitmap(it1) }


                                    places.add(ClinicData(place.name, place.address, it, bitmap, MarkerOptions()))
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(it)
                                            .title(place.name)
                                            .icon(icon)
                                    )
                                }
                            }
                        }
                        showPlacesInRecyclerView(places)
                    } else {
                        Toast.makeText(requireContext(), "Failed to find nearby clinics", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showPlacesInRecyclerView(places: List<ClinicData>) {
        val clinicRv = binding.clinicRv
        clinicRv.layoutManager = LinearLayoutManager(requireContext())
        val clinicAdapter = ClinicAdapter(places)
        clinicAdapter.setOnItemClickListener(this)
        clinicRv.adapter = clinicAdapter
    }

    override fun onClinicItemClick(clinicData: ClinicData) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(clinicData.location, 17f))
    }

    private fun showLoading(onLoading: Boolean){
        if(onLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap)
            }
        }
    }

    override fun onLocationEnabled() {
        if (::mMap.isInitialized) {
            mMap.clear()
            loadCurrentLocation()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}