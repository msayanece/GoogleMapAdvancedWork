package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sayan.rnd.googlemapadvancedwork.R;

import java.util.ArrayList;

public class MapPlaybackViewHolder {
    private Activity activity;
    private ImageView buttonPlay;
    private ImageView buttonPause;
    private LinearLayout linearLayoutBottomBar;
    private TextView addressTextView;
    private TextView dateTimeTextView;
    private AppCompatSeekBar seekBar;

    public AppCompatSeekBar getSeekBar() {
        return seekBar;
    }

    public ImageView getButtonPlay() {
        return buttonPlay;
    }

    public ImageView getButtonPause() {
        return buttonPause;
    }

    public MapPlaybackViewHolder() {}

    public MapPlaybackViewHolder(Activity activity) {
        this.activity = activity;
        initializeViews();
    }

    private void initializeViews() {
        //bottom Bar
        linearLayoutBottomBar = activity.findViewById(R.id.linearLayoutBottomBar);
        addressTextView = linearLayoutBottomBar.findViewById(R.id.addressTextView);
        dateTimeTextView = linearLayoutBottomBar.findViewById(R.id.dateTimeTextView);
//        linearLayoutBottomBar.setVisibility(View.GONE);

        //Play back bar
        LinearLayout linearLayoutPlayback = activity.findViewById(R.id.linearLayoutPlayback);
        buttonPlay = linearLayoutPlayback.findViewById(R.id.buttonPlay);
        buttonPause = linearLayoutPlayback.findViewById(R.id.buttonPause);
        seekBar = linearLayoutPlayback.findViewById(R.id.seekBar);
    }

    public void changePlayPauseButtonToPause() {
        buttonPlay.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
    }

    public void changePlayPauseButtonToPlay() {
        buttonPlay.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.GONE);
    }

    public Polyline initializePolyLine(GoogleMap googleMap, ArrayList<Marker> markers) {
        //polyLinePoints = new ArrayList<LatLng>();
        PolylineOptions rectOptions = new PolylineOptions();
        rectOptions.add(markers.get(0).getPosition());
        rectOptions.color(Color.BLUE);
        rectOptions.geodesic(false);
        rectOptions.width(6f);
        return googleMap.addPolyline(rectOptions);
    }

    /**
     * Map marker util method. Draw a custom circular marker on Map
     * @param latitude latitude of the marker position
     * @param longitude longitude of the marker position
     * @param title the marker title
     * @param googleMap the map on which the marker needs to be draw
     * @return the created marker object
     */
    public Marker drawMarker(double latitude, double longitude, String title, GoogleMap googleMap) {
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
                activity,
                marker,
                "https://oc2.ocstatic.com/images/logo_small.png"
        );
        return marker;
    }

    @NonNull
    public Marker addNewSnippetMarkerToMap(LatLng latLng, GoogleMap googleMap) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("title")
                .snippet("snippet"));
        marker.setVisible(false);
        return marker;
    }

    /**
     * Map marker util method. Draw a custom circular marker on Map
     * @param latitude latitude of the marker position
     * @param longitude longitude of the marker position
     * @param title the marker title
     * @param googleMap the map on which the marker needs to be draw
     * @return the created marker object
     */
    public Marker drawMarker(double latitude, double longitude, String title, GoogleMap googleMap, boolean shouldAnimateCamera, boolean shouldMoveCamera) {
        //create and draw the marker
        Marker marker = MarkerStyleUtil.setMarkerInMap(
                googleMap,
                title,
                latitude,
                longitude,
                shouldAnimateCamera,
                shouldMoveCamera
        );

        //customize the marker: make it circular
        MarkerStyleUtil.setCustomMarkerCircular(
                activity,
                marker,
                "https://oc2.ocstatic.com/images/logo_small.png"
        );
        return marker;
    }

    public void notifyDataSetChanged(String currentDateTime, String currentAddress, int currentProgress){
        if (currentDateTime != null) {
            dateTimeTextView.setText(currentDateTime);
        }
        if (currentAddress != null) {
            addressTextView.setText(currentAddress);
        }
        seekBar.setProgress(currentProgress);
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

}
