package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.LocationDataHolder;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsAnimationPlaybackActivity extends AppCompatActivity {

    private static boolean isActivityInForeground = false;
    private ImageView buttonPlay;
    private ImageView buttonPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_animation_playback);
        initializeViews();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapPlaybackController.getInstance(this, googleMap, );
        googleMap.clear();
        points = new ArrayList<>();
        dates = new ArrayList<>();
        addresses = new ArrayList<>();
        markers = new ArrayList<>();
        ArrayList<LatLng> locationsJSON = LocationDataHolder.getLocationsJSON();
        try {
            JSONObject fromJsonObject = locationsJSON.getJSONObject(0);
            JSONObject toJsonObject = locationsJSON.getJSONObject(locationsJSON.length() - 1);
            fromLatitude = fromJsonObject.getDouble("latitude");
            fromLongitude = fromJsonObject.getDouble("longitude");

            playbackMarker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(fromLatitude,fromLongitude))
                            .title(title)
            );
            playbackMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(marker)));
            playbackMarker.hideInfoWindow();
            cameraPosition =
                    new CameraPosition.Builder()
                            .target(new LatLng(fromLatitude,fromLongitude))
                            .bearing(15)
                            .tilt(15)
                            .zoom(googleMap.getCameraPosition().zoom)
                            .build();

//            double toLatitude = toJsonObject.getDouble("latitude");
//            double toLongitude = toJsonObject.getDouble("longitude");

            drawMarker(fromLatitude, fromLongitude, "From", mMap);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fromLatitude, fromLongitude)));
//            drawMarker(toLatitude, toLongitude, "To", mMap);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(toLatitude, toLongitude)));
//            String url = getUrl(fromLatitude, fromLongitude, toLatitude, toLongitude);
//            FetchUrl FetchUrl = new FetchUrl();
//            FetchUrl.execute(url);
            groupHistoryTrackingMapData(userid, mMap);
        }catch (JSONException e){
            e.printStackTrace();
        }
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
