package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities

import android.content.pm.ActivityInfo
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sayan.rnd.googlemapadvancedwork.R
import com.google.maps.android.clustering.ClusterManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationFalureListener
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationSuccessListener
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationFetchHelper
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.markerclusteringrelated.MarkerClusteringDataHolder


class MapsClusteredMarkersActivity : AppCompatActivity(), OnMapReadyCallback, FetchLocationSuccessListener, FetchLocationFalureListener {
    override fun onLocationFetchFailed(errorMessage: String?) {
        Toast.makeText(this, "Location permission denied: $errorMessage", Toast.LENGTH_LONG).show()
    }

    override fun onLocationFetched(location: Location?) {
        location?.let {
            setUpClusters(location.latitude, location.longitude)
        }
    }

    // Declare a variable for the cluster manager.
    private var mClusterManager: ClusterManager<MarkerItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_clustered_markers)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.markerClusteringMap) as SupportMapFragment?
        if (mapFragment != null)
            mapFragment.getMapAsync(this)
        else {
            Toast.makeText(this, "Unable to load map", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MarkerClusteringDataHolder.getInstance().googleMap = googleMap
        LocationFetchHelper(
                this,
                this,
                this,
                false
        )
    }

    private fun setUpClusters(lat: Double, lon: Double) {

        // Position the map.
        MarkerClusteringDataHolder.getInstance().googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 10f))
        MarkerClusteringDataHolder.getInstance().googleMap.uiSettings.isZoomControlsEnabled = true

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = ClusterManager<MarkerItem>(this, MarkerClusteringDataHolder.getInstance().googleMap)

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        MarkerClusteringDataHolder.getInstance().googleMap.setOnCameraIdleListener(mClusterManager)
        MarkerClusteringDataHolder.getInstance().googleMap.setOnMarkerClickListener(mClusterManager)

        // Add cluster items (markers) to the cluster manager.
        addItems(lat, lon)
    }

    private fun addItems(latitude: Double, longitude: Double) {

        // Set some lat/lng coordinates to start with.
        var lat = latitude
        var lng = longitude

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..29) {
            val offset = i / 60.0
            if (i <10){
                lat += offset
                lng -= offset
            }else if(i <20){
                lat -= offset
                lng += offset
            }else if(i <25){
                lat += offset
                lng += offset
            }else {
                lat -= offset
                lng -= offset
            }
            val offsetItem = MarkerItem(lat, lng)
            mClusterManager?.addItem(offsetItem)
        }
    }
}
