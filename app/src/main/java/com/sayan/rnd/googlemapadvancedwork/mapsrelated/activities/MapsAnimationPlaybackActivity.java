package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.LocationDataHolder;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackController;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MarkerStyleUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsAnimationPlaybackActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static boolean isActivityInForeground = false;
    private ImageView buttonPlay;
    private ImageView buttonPause;

    private double fromLatitude;
    private double fromLongitude;
    private double toLatitude;
    private double toLongitude;

    private Marker playbackMarker;
    private String title = "Marker";
    private CameraPosition cameraPosition;

    private ArrayList<Object> points;
    private ArrayList<Object> dates;
    private ArrayList<Object> addresses;
    private ArrayList<Object> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_animation_playback);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.playbackMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        else {
            Toast.makeText(this, "Unable to load map", Toast.LENGTH_SHORT).show();
        }
        initializeViews();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapPlaybackController.getInstance(this, googleMap);
        googleMap.clear();
        points = new ArrayList<>();
        dates = new ArrayList<>();
        addresses = new ArrayList<>();
        markers = new ArrayList<>();
        ArrayList<LatLng> locationsJSON = LocationDataHolder.getLocationsJSON();
        fromLatitude = locationsJSON.get(0).latitude;
        fromLongitude = locationsJSON.get(0).longitude;

        toLatitude = locationsJSON.get(locationsJSON.size() - 1).longitude;
        toLongitude = locationsJSON.get(locationsJSON.size() - 1).longitude;

        playbackMarker = MarkerStyleUtil.setMarkerInMap(
                googleMap,
                title,
                fromLatitude,
                fromLongitude,
                false,
                false
        );

        MarkerStyleUtil.setCustomMarkerCircular(
                this,
                playbackMarker,
                "https://oc2.ocstatic.com/images/logo_small.png"
        );

        playbackMarker.hideInfoWindow();
//        cameraPosition =
//                new CameraPosition.Builder()
//                        .target(new LatLng(fromLatitude,fromLongitude))
//                        .bearing(15)
//                        .tilt(15)
//                        .zoom(googleMap.getCameraPosition().zoom)
//                        .build();

        Marker fromMarker = drawMarker(fromLatitude, fromLongitude, "From", googleMap);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fromLatitude, fromLongitude)));
        Marker toMarker = drawMarker(toLatitude, toLongitude, "To", googleMap);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(toLatitude, toLongitude)));
    }

    private Marker drawMarker(double latitude, double longitude, String title, GoogleMap googleMap) {
        Marker marker = MarkerStyleUtil.setMarkerInMap(
                googleMap,
                title,
                latitude,
                longitude,
                false,
                false
        );

        MarkerStyleUtil.setCustomMarkerCircular(
                this,
                marker,
                "https://oc2.ocstatic.com/images/logo_small.png"
        );
        return marker;
    }

    private void initializeViews() {
        //bottom Bar
        LinearLayout linearLayoutBottomBar = findViewById(R.id.linearLayoutBottomBar);
        TextView addressTextView = linearLayoutBottomBar.findViewById(R.id.addressTextView);
        TextView dateTimeTextView = linearLayoutBottomBar.findViewById(R.id.dateTimeTextView);
//        linearLayoutBottomBar.setVisibility(View.GONE);

        //Play back bar
        LinearLayout linearLayoutPlayback = findViewById(R.id.linearLayoutPlayback);
        buttonPlay = linearLayoutPlayback.findViewById(R.id.buttonPlay);
        buttonPause = linearLayoutPlayback.findViewById(R.id.buttonPause);
        AppCompatSeekBar seekBar = linearLayoutPlayback.findViewById(R.id.seekBar);
    }

    public void playPauseButtonOnClick(View view) {
        switch (view.getId()){
            case R.id.buttonPlay:
                if (MapPlaybackController.getInstance() != null){
                    MapPlaybackController.getInstance().onClickPlay();
                }else {
                    Toast.makeText(this, "Please wait for Map to load!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonPause:
                if (MapPlaybackController.getInstance() != null){
                    MapPlaybackController.getInstance().onClickPause();
                }else {
                    Toast.makeText(this, "Please wait for Map to load!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityInForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityInForeground = false;
    }

    public static boolean isForeground(){
        return isActivityInForeground;
    }
}
