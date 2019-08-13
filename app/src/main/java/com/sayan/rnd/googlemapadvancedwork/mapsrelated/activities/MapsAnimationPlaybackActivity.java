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
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackDataHolder;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MarkerStyleUtil;

import java.util.ArrayList;

import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.ANIMATION_DEFAULT;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.DELAY;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.PLAYBACK_MARKER_TITLE;

public class MapsAnimationPlaybackActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static boolean isActivityInForeground = false;
    private MapPlaybackController mapPlaybackController;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double fromLatitude = 0.0;
        double fromLongitude = 0.0;
        double toLatitude = 0.0;
        double toLongitude = 0.0;
        //First time initialize MapPlaybackController with googleMap so that in future we can use it later
        mapPlaybackController = MapPlaybackController.getInstance(this, googleMap);
        //clear the map
        googleMap.clear();
        //get from location and to location data
        ArrayList<LatLng> locations = LocationDataHolder.getLocationsJSON();
        fromLatitude = locations.get(0).latitude;
        fromLongitude = locations.get(0).longitude;
        toLatitude = locations.get(locations.size() - 1).longitude;
        toLongitude = locations.get(locations.size() - 1).longitude;
        //draw the marker responsible for play back. disable info window
        Marker playbackMarker = mapPlaybackController.getMapPlaybackViewHolder().drawMarker(
                fromLatitude, fromLongitude, PLAYBACK_MARKER_TITLE, googleMap
        );
        saveFromToLocationOnDataHolder(fromLatitude, fromLongitude, toLatitude, toLongitude, mapPlaybackController);
        playbackMarker.hideInfoWindow();
        //focus map camera to the initial from location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(fromLatitude, fromLongitude))
                .bearing(15)
                .tilt(15)
                .zoom(googleMap.getCameraPosition().zoom)
                .build();
        //draw marker on from position
        Marker fromMarker = mapPlaybackController.getMapPlaybackViewHolder().drawMarker(
                fromLatitude, fromLongitude, "From", googleMap
        );
        //move the map camera to initial from location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fromLatitude, fromLongitude)));
        /*//draw marker on to position
        Marker toMarker = drawMarker(toLatitude, toLongitude, "To", googleMap);*/
    }

    private void saveFromToLocationOnDataHolder(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude, MapPlaybackController mapPlaybackController) {
        MapPlaybackDataHolder mapPlaybackDataHolder = mapPlaybackController.getMapPlaybackDataHolder();
        mapPlaybackDataHolder.setFromLatitude(fromLatitude);
        mapPlaybackDataHolder.setFromLongitude(fromLongitude);
        mapPlaybackDataHolder.setToLatitude(toLatitude);
        mapPlaybackDataHolder.setToLongitude(toLongitude);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mapPlaybackController.clearMap();
    }

    public static boolean isForeground(){
        return isActivityInForeground;
    }
}
