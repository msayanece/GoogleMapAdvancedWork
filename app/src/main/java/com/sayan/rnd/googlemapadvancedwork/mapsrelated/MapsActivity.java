package com.sayan.rnd.googlemapadvancedwork.mapsrelated;

import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationFalureListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationSuccessListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationFetchHelper;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private boolean isFirstLoading;
    private Marker myCurrentLocation;

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
        fetchLocation(googleMap);
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
            if (CoordinateUtil.getDistance(position.latitude, position.longitude, latitude, longitude) > 0.01) {
                MarkerStyleUtil.animateMarker(googleMap, myCurrentLocation, new LatLng(latitude, longitude), false);
            }
        }
    }




}
