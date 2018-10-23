package com.sayan.rnd.googlemapadvancedwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationFalureListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.FetchLocationSuccessListener;
import com.sayan.rnd.googlemapadvancedwork.locationfetchrelated.LocationFetchHelper;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

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
        fetchLocation(googleMap);
    }

    private void fetchLocation(final GoogleMap googleMap){
        new LocationFetchHelper(this, new FetchLocationSuccessListener() {
            @Override
            public void onLocationFetched(double latitude, double longitude) {
                Toast.makeText(MapsActivity.this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_LONG).show();
                setLatLonInMap(latitude, longitude, googleMap);
            }
        }, new FetchLocationFalureListener() {
            @Override
            public void onLocationFetchFailed(String errorMessage) {
                Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }, false);
    }

    private void setLatLonInMap(double latitude, double longitude, GoogleMap googleMap) {
        LatLng latLng = latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

}
