package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationFalureListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationSuccessListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationFetchHelper;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationPermissionListener;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.CoordinateUtil;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MarkerStyleUtil;

public class MapsActivity extends AppCompatActivity implements SensorEventListener, LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long LOCATION_FASTEST_INTERVAL = 5 * 1000;
    private static final long LOCATION_INTERVAL = 20 * 1000;
    private boolean isFirstLoading;
    private Marker myCurrentLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;

    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];
    private SensorManager mSensorManager;
    private boolean isSensorAccuracyLow;
    private float bearingInDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        /*uncomment this line of code if you want to use onLocationChange of LocationListener class.
         * also uncomment the implements interface of LocationListener in this activity
         */
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapsActivity.this);
//        LocationServices.getFusedLocationProviderClient(this).
//                requestLocationUpdates(mLocationRequest, new MyLocationCallback(this), Looper.myLooper());
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
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(0);
        return locationRequest;
    }

    private void fetchLocation(final GoogleMap googleMap) {
        new LocationFetchHelper(this, new FetchLocationSuccessListener() {
            @Override
            public void onLocationFetched(Location location) {
//                Toast.makeText(MapsActivity.this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_LONG).show();
                setMarkerWork(googleMap, location);
            }
        }, new FetchLocationFalureListener() {
            @Override
            public void onLocationFetchFailed(String errorMessage) {
                Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }
        }, true);
    }

    private void setMarkerWork(GoogleMap googleMap, Location location) {
        if (isFirstLoading) {
            myCurrentLocationMarker = MarkerStyleUtil.setMarkerInMap(googleMap, "My Location", location.getLatitude(), location.getLongitude(), true, false);
//            MarkerStyleUtil.setCustomMarkerCircular(this, myCurrentLocationMarker, "https://www.eve24hrs.com/upload/userprofilepicture/user_profile_201810091038431385949295.jpg");
            MarkerStyleUtil.setCustomMarkerFlat(this, myCurrentLocationMarker, "https://www.eve24hrs.com/upload/userprofilepicture/user_profile_201810091038431385949295.jpg", location, 0.5f, 0.6f);
            isFirstLoading = false;
        } else {
            LatLng position = myCurrentLocationMarker.getPosition();
            if (CoordinateUtil.getDistance(position.latitude, position.longitude, location.getLatitude(), location.getLongitude()) > 0.001) {
                MarkerStyleUtil.animateMarker(googleMap, myCurrentLocationMarker, location, false, false, false);
            }
        }
    }

    //override method if you are using LocationListener. Else, simple method for next work
    @SuppressLint("MissingPermission")
    public void onLocationChanged(Location location) {
        setMarkerWork(mGoogleMap, location);
        mGoogleMap.setMyLocationEnabled(true);
    }

    //comment this if you are using LocationListener
//    private static class MyLocationCallback extends LocationCallback {
//        private MapsActivity mapsActivity;
//
//        MyLocationCallback(MapsActivity mapsActivity) {
//            this.mapsActivity = mapsActivity;
//        }
//
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            super.onLocationResult(locationResult);
//            try {
//                mapsActivity.onLocationChanged(locationResult.getLastLocation());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    //region Other activity override methods
    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            mSensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        mSensorManager.unregisterListener(this);
    }
    //endregion

    //region Sensor related work for fetching device rotation
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW){
//            Toast.makeText(this, "Sensor accuracy low, Please rotate your device in a 8 shape!", Toast.LENGTH_LONG).show();
            isSensorAccuracyLow = true;
        }
        int sensorType = event.sensor.getType();
        /*
        * set mAccelerometerData or mMagnetometerData
         */
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = event.values.clone();
                break;
            default:
                return;
        }
        /*
        * generate a rotation matrix which will in terms supply rotation to getOrientation method
         */
        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);

        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        /*
        * azimuth = angle in radian north/south/east/west
        * pitch = angle in radian top/bottom tilt
        * roll = angle in radian left/right tilt
         */
        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];
        /*
        * set the rotation to the marker
         */
        float bearingInDegreeNew = (float) CoordinateUtil.rad2deg(azimuth);
        float bearingDifference = CoordinateUtil.findBearingDifference(bearingInDegree, bearingInDegreeNew);
        if (event.accuracy != SensorManager.SENSOR_STATUS_ACCURACY_LOW && bearingDifference <= 30.0f && bearingDifference > 2.0f) {
            if (myCurrentLocationMarker != null) {
                myCurrentLocationMarker.setRotation(bearingInDegree);
            }
        }
        bearingInDegree = bearingInDegreeNew;
//        Log.e("onSensorChanged: ", String.valueOf(bearingInDegree));
        Log.e("onSensorChanged: ", String.valueOf(azimuth));
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (isSensorAccuracyLow) {
            if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
                Toast.makeText(this, "Sensor accuracy gets better, keep it up!", Toast.LENGTH_LONG).show();
            }
        }
    }
    //endregion
}
