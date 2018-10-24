package com.sayan.rnd.googlemapadvancedwork.mapsrelated;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationFalureListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationSuccessListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationFetchHelper;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationFetchHelperSingleton;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationPermissionListener;

public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long LOCATION_FASTEST_INTERVAL = 5 * 1000;
    private static final long LOCATION_INTERVAL = 20 * 1000;
    private boolean isFirstLoading;
    private Marker myCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        else {
            Toast.makeText(this, "Unable to load map", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isFirstLoading = true;
//        fetchLocation(googleMap);
        mGoogleMap = googleMap;
        setUpGoogleApiClient();
    }

    private void setUpGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = createLocationRequest();
        new LocationFetchHelper(this, mLocationRequest, new LocationPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MapsActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                startLocationUpdates();
            }

            @Override
            public void onPermissionDenied(String errorMessage) {
                Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapsActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Google API connection suspended.", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.clearDefaultAccountAndReconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
//        mGoogleApiClient.clearDefaultAccountAndReconnect();
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    private void fetchLocation(final GoogleMap googleMap) {
        new LocationFetchHelper(this, new FetchLocationSuccessListener() {
            @Override
            public void onLocationFetched(double latitude, double longitude) {
//                Toast.makeText(MapsActivity.this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_LONG).show();
                setMarkerWork(googleMap, latitude, longitude);
            }
        }, new FetchLocationFalureListener() {
            @Override
            public void onLocationFetchFailed(String errorMessage) {
                Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }
        }, true);
    }

    private void setMarkerWork(GoogleMap googleMap, double latitude, double longitude) {
        if (isFirstLoading) {
            myCurrentLocation = MarkerStyleUtil.setMarkerInMap(googleMap, "My Location", latitude, longitude, true, false);
            isFirstLoading = false;
        } else {
            LatLng position = myCurrentLocation.getPosition();
            if (CoordinateUtil.getDistance(position.latitude, position.longitude, latitude, longitude) > 0.001) {
                MarkerStyleUtil.animateMarker(googleMap, myCurrentLocation, new LatLng(latitude, longitude), false);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setMarkerWork(mGoogleMap, location.getLatitude(), location.getLongitude());
    }
}
