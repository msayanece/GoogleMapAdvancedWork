package com.sayan.rnd.googlemapadvancedwork.mapsrelated;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationPermissionListener;

public class MapsActivity extends AppCompatActivity implements /*LocationListener, */OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long LOCATION_FASTEST_INTERVAL = 5 * 1000;
    private static final long LOCATION_INTERVAL = 20 * 1000;
    private boolean isFirstLoading;
    private Marker myCurrentLocationMarker;
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
        //uncomment this line of code if you want to use onLocationChange of LocationListener class.
        // also uncomment the implements interface of LocationListener in this activity
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapsActivity.this);
        LocationServices.getFusedLocationProviderClient(this).
                requestLocationUpdates(mLocationRequest, new MyLocationCallback(this), Looper.myLooper());
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
            myCurrentLocationMarker = MarkerStyleUtil.setMarkerInMap(googleMap, "My Location", latitude, longitude, true, false);
            MarkerStyleUtil.setCustomMarkerCircular(this, myCurrentLocationMarker, "https://www.eve24hrs.com/upload/userprofilepicture/user_profile_201810091038431385949295.jpg");
            isFirstLoading = false;
        } else {
            LatLng position = myCurrentLocationMarker.getPosition();
            if (CoordinateUtil.getDistance(position.latitude, position.longitude, latitude, longitude) > 0.001) {
                MarkerStyleUtil.animateMarker(googleMap, myCurrentLocationMarker, new LatLng(latitude, longitude), false);
            }
        }
    }

    //override method if you are using LocationListener. Else, simple method for next work
    public void onLocationChanged(Location location) {
        setMarkerWork(mGoogleMap, location.getLatitude(), location.getLongitude());
    }

    //comment this if you are using LocationListener
    private static class MyLocationCallback extends LocationCallback {
        private MapsActivity mapsActivity;

        MyLocationCallback(MapsActivity mapsActivity) {
            this.mapsActivity = mapsActivity;
        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            try {
                mapsActivity.onLocationChanged(locationResult.getLastLocation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
