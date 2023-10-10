package com.sonai.bloodlink.utilityClasses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class LocationGets(val context: Context) {

    // Address to get lat and long
    fun getLocationLatLong(locationName: String): Address? {
        locationRequest()
        val geocoder = Geocoder(context)
        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocationName(locationName, 1) as List<Address>
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            return address
        } else {
            // Handle location not found
            return null
        }
    }

    //Location allow or deny
    fun locationAllowOrDeny(): Boolean{
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val isPermissionGranted = ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
        if (!isPermissionGranted) {
            return false
        }
        return true
    }

    // Current address
    fun locationRequest() {
        if (!isLocationEnabled()){
            Toast.makeText(context, "Enable Location Service", Toast.LENGTH_SHORT).show()
            requestLocationService()
        }else {
            val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
            val isPermissionGranted = ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED
            if (!isPermissionGranted) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(permission),
                    101
                )
            }
        }
    }

    //GPS on or not
    private fun isLocationEnabled(): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }

    // Function to prompt the user to enable location services
    private fun requestLocationService() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    // Get the user's current location as an Address object
    fun getCurrentLocation(callback: (Address?) -> Unit) {
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val isPermissionGranted = ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
        if (!isPermissionGranted) {
            // Location permission is not granted, request it
            callback(null)
        } else {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses!!.isNotEmpty()) {
                            // Return the Address object
                            callback(addresses[0])
                        } else {
                            // Address not found
                            callback(null)
                        }
                    } else {
                        // Location is not available
                        callback(null)
                    }
                }
                .addOnFailureListener {
                    // Handle any errors here
                    callback(null)
                }
        }
    }


}
