package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.LocationDataHolder;

import java.util.ArrayList;

public class MapsAnimationPlayback extends AppCompatActivity {

    private ImageView buttonPlay;
    private ImageView buttonPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_animation_playback);
        initializeViews();
        ArrayList<LatLng> locationsJSON = LocationDataHolder.getLocationsJSON();
    }

    private void initializeViews() {
        //bottom Bar
        LinearLayout linearLayoutBottomBar = findViewById(R.id.linearLayoutBottomBar);
        TextView addressTextView = linearLayoutBottomBar.findViewById(R.id.addressTextView);
        TextView dateTimeTextView = linearLayoutBottomBar.findViewById(R.id.dateTimeTextView);
        linearLayoutBottomBar.setVisibility(View.GONE);

        //Play back bar
        LinearLayout linearLayoutPlayback = findViewById(R.id.linearLayoutPlayback);
        buttonPlay = linearLayoutPlayback.findViewById(R.id.buttonPlay);
        buttonPause = linearLayoutPlayback.findViewById(R.id.buttonPause);
        ImageView seekBar = linearLayoutPlayback.findViewById(R.id.seekBar);
    }

    public void playPauseButtonOnClick(View view) {
        switch (view.getId()){
            case R.id.buttonPlay:
                onClickPlay();
                break;
            case R.id.buttonPause:
                onClickPause();
                break;
        }
    }

    private void onClickPlay() {
        //TODO uncomment these lines
//        if (currentPt == markers.size()-1){
//            onMapReady(mMap);
//        }else {
//            if (playPauseLatitude != 0.0 && playPauseLongitude != 0.0) {
//                playAnimation();
//            }
//        }
//        buttonPlay.setVisibility(View.GONE);
//        buttonPause.setVisibility(View.VISIBLE);
    }

    private void onClickPause() {
        //TODO uncomment these lines
//        playPauseLatitude = markers.get(currentPt).getPosition().latitude;
//        playPauseLongitude = markers.get(currentPt).getPosition().longitude;
//        pauseAnimation();
//        buttonPlay.setVisibility(View.VISIBLE);
//        buttonPause.setVisibility(View.GONE);
    }

}
