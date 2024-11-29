package com.ag.projects.aatask.util.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.Locale

fun getAddressFromLatLng(context: Context, lat: Double, lng: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val addresses: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val addressComponents = listOf(
                address.countryName,   // Country
                address.locality,      // City
                address.adminArea,     // State
            ).filterNot { it.isNullOrEmpty() }

            return addressComponents.joinToString(separator = ", ")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return "Address not found"
}