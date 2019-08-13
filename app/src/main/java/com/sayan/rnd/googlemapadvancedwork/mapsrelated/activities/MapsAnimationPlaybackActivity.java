package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.LocationDataHolder;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapAnimationUtil;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackController;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MarkerStyleUtil;

import java.util.ArrayList;

import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.ANIMATION_DEFAULT;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.DELAY;

public class MapsAnimationPlaybackActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static boolean isActivityInForeground = false;
    private ImageView buttonPlay;
    private ImageView buttonPause;

    private double fromLatitude;
    private double fromLongitude;
    private double toLatitude;
    private double toLongitude;

    private String title = "Marker";
    private CameraPosition cameraPosition;

    //View properties
    private LinearLayout linearLayoutBottomBar;
    private TextView addressTextView;
    private TextView dateTimeTextView;
    private AppCompatSeekBar seekBar;

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
        //First time initialize MapPlaybackController with googleMap so that in future we can use it later
        MapPlaybackController mapPlaybackController = MapPlaybackController.getInstance(this, googleMap);
        //clear the map
        googleMap.clear();
        //get from location and to location data
        ArrayList<LatLng> locations = LocationDataHolder.getLocationsJSON();
        fromLatitude = locations.get(0).latitude;
        fromLongitude = locations.get(0).longitude;
        toLatitude = locations.get(locations.size() - 1).longitude;
        toLongitude = locations.get(locations.size() - 1).longitude;
        //draw the marker responsible for play back. disable info window
        Marker playbackMarker = drawMarker(fromLatitude, fromLongitude, title, googleMap);
        mapPlaybackController.setPlaybackMarker(playbackMarker);
        playbackMarker.hideInfoWindow();
        //focus map camera to the initial from location
        cameraPosition =
                new CameraPosition.Builder()
                        .target(new LatLng(fromLatitude,fromLongitude))
                        .bearing(15)
                        .tilt(15)
                        .zoom(googleMap.getCameraPosition().zoom)
                        .build();
        //draw marker on from position
        Marker fromMarker = drawMarker(fromLatitude, fromLongitude, "From", googleMap);
        //move the map camera to initial from location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fromLatitude, fromLongitude)));
        /*//draw marker on to position
        Marker toMarker = drawMarker(toLatitude, toLongitude, "To", googleMap);*/
    }



    /**
     * Map marker util method. Draw a custom circular marker on Map
     * @param latitude latitude of the marker position
     * @param longitude longitude of the marker position
     * @param title the marker title
     * @param googleMap the map on which the marker needs to be draw
     * @return the created marker object
     */
    private Marker drawMarker(double latitude, double longitude, String title, GoogleMap googleMap) {
        //create and draw the marker
        Marker marker = MarkerStyleUtil.setMarkerInMap(
                googleMap,
                title,
                latitude,
                longitude,
                false,
                false
        );

        //customize the marker: make it circular
        MarkerStyleUtil.setCustomMarkerCircular(
                this,
                marker,
                "https://oc2.ocstatic.com/images/logo_small.png"
        );
        return marker;
    }

    private void initializeViews() {
        //bottom Bar
        linearLayoutBottomBar = findViewById(R.id.linearLayoutBottomBar);
        addressTextView = linearLayoutBottomBar.findViewById(R.id.addressTextView);
        dateTimeTextView = linearLayoutBottomBar.findViewById(R.id.dateTimeTextView);
//        linearLayoutBottomBar.setVisibility(View.GONE);

        //Play back bar
        LinearLayout linearLayoutPlayback = findViewById(R.id.linearLayoutPlayback);
        buttonPlay = linearLayoutPlayback.findViewById(R.id.buttonPlay);
        buttonPause = linearLayoutPlayback.findViewById(R.id.buttonPause);
        seekBar = linearLayoutPlayback.findViewById(R.id.seekBar);
    }

    public void notifyDataSetChanged(String currentDateTime, String currentAddress, int currentProgress){
        dateTimeTextView.setText(currentDateTime);
        if (currentAddress != null) {
            addressTextView.setText(currentAddress);
        }
        seekBar.setProgress(currentProgress);
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
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
