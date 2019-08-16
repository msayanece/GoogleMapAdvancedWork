package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities

import android.content.Context
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationFalureListener
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationSuccessListener
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationFetchHelper
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.markerclusteringrelated.MarkerClusteringDataHolder


class MapsClusteredMarkersActivity :
        AppCompatActivity(),
        OnMapReadyCallback,
        FetchLocationSuccessListener,
        FetchLocationFalureListener,
        ClusterManager.OnClusterClickListener<MarkerItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MarkerItem>,
        ClusterManager.OnClusterItemClickListener<MarkerItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem> {
    override fun onClusterItemInfoWindowClick(p0: MarkerItem?) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    override fun onClusterItemClick(markerItem: MarkerItem?): Boolean {
        // Does nothing, but you could go into the user's profile page, for example.
        return false
    }

    override fun onClusterInfoWindowClick(markerItems: Cluster<MarkerItem>?) {
        // Does nothing, but you could go to a list of the users.
    }

    override fun onClusterClick(cluster: Cluster<MarkerItem>?): Boolean {
        // Show a toast with some info when the cluster is clicked.
        val firstName = cluster?.items?.iterator()?.next()?.title
        showToast("${cluster?.size} (including $firstName)")
        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        val builder = LatLngBounds.builder()
        cluster?.let {
            for (item in it.items) {
                builder.include(item.position)
            }
        }
        // Get the LatLngBounds
        val bounds = builder.build()
        // Animate camera to the bounds
        try {
            MarkerClusteringDataHolder.getInstance().googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

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
            showToast("Unable to load map")
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
        mClusterManager?.let {
            it.renderer = MarkerClusterRenderer(
                    this,
                    MarkerClusteringDataHolder.getInstance().googleMap,
                    it
            )
            it.setOnClusterClickListener(this)
            it.setOnClusterInfoWindowClickListener(this)
            it.setOnClusterItemClickListener(this)
            it.setOnClusterItemInfoWindowClickListener(this)
        }
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        MarkerClusteringDataHolder.getInstance().googleMap.let {
            it.setOnCameraIdleListener(mClusterManager)
            it.setOnMarkerClickListener(mClusterManager)
            it.setOnInfoWindowClickListener(mClusterManager)
        }

        // Add cluster items (markers) to the cluster manager.
        addItems(lat, lon)

        mClusterManager?.cluster()
    }

    private fun addItems(latitude: Double, longitude: Double) {

        // Set some lat/lng coordinates to start with.
        var lat = latitude
        var lng = longitude

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..29) {

            val offset = i / 900.0
            when {
                i < 2 -> {
                }
                i < 10 -> {
                    lat += offset
                    lng -= offset
                }
                i < 20 -> {
                    lat -= offset
                    lng += offset
                }
                i < 25 -> {
                    lat += offset
                    lng += offset
                }
                else -> {
                    lat -= offset
                    lng -= offset
                }
            }

            // Create a cluster item for the marker and set the title and snippet using the constructor.
            val infoWindowItem  = when {
                i%3 == 0 -> MarkerItem(lat, lng, "Gran", "This is Granny", R.drawable.gran)
                i%5 == 0 -> MarkerItem(lat, lng, "Yeats", "This is Yeats", R.drawable.yeats)
                i%2 == 0 -> MarkerItem(lat, lng, "Walter", "This is Walter", R.drawable.walter)
                else -> MarkerItem(lat, lng, "Turtle", "Trevor the Turtle", R.drawable.noimage)
            }
            // Add the cluster item (marker) to the cluster manager.
            mClusterManager?.addItem(infoWindowItem)
        }
    }
}
