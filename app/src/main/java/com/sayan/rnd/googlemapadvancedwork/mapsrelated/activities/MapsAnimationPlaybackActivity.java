package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities;

import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sayan.rnd.googlemapadvancedwork.R;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.LocationDataHolder;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated.MapPlaybackController;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated.MapPlaybackDataHolder;

import java.util.ArrayList;

import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated.MapPlaybackConstants.PLAYBACK_MARKER_TITLE;

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
        } else {
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
        //get from location and to location data
        ArrayList<LatLng> locations = LocationDataHolder.getLocationsJSON();
        fromLatitude = locations.get(0).latitude;
        fromLongitude = locations.get(0).longitude;
        toLatitude = locations.get(locations.size() - 1).longitude;
        toLongitude = locations.get(locations.size() - 1).longitude;
        //initialize SeekBar
        mapPlaybackController.initializeSeekBar(locations.size() - 1);
        saveFromToLocationOnDataHolder(fromLatitude, fromLongitude, toLatitude, toLongitude, mapPlaybackController);
        //focus map camera to the initial from location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(fromLatitude, fromLongitude))
                .bearing(15)
                .tilt(15)
                .zoom(googleMap.getCameraPosition().zoom)
                .build();
        //draw marker on from position
        Marker fromMarker = mapPlaybackController.getMapPlaybackViewHolder().drawMarker(
                fromLatitude, fromLongitude, "From", googleMap,
                "https://interactive-examples.mdn.mozilla.net/media/examples/grapefruit-slice-332-332.jpg"
        );
        //draw the marker responsible for play back. disable info window
        Marker playbackMarker = mapPlaybackController.getMapPlaybackViewHolder().drawMarker(
                fromLatitude, fromLongitude, PLAYBACK_MARKER_TITLE, googleMap,
                "https://oc2.ocstatic.com/images/logo_small.png"
        );
        playbackMarker.hideInfoWindow();
        mapPlaybackController.getMapPlaybackDataHolder().setPlaybackMarker(playbackMarker);
        //move the map camera to initial from location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fromLatitude, fromLongitude)));
        prepareDataAndStartAnimation(googleMap, locations);
        /*//draw marker on to position
        Marker toMarker = drawMarker(toLatitude, toLongitude, "To", googleMap);*/
    }

    public void prepareDataAndStartAnimation(GoogleMap googleMap, ArrayList<LatLng> locations) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setMaxZoomPreference(19f);
        googleMap.setMinZoomPreference(5f);
        mapPlaybackController.getMapPlaybackDataHolder().getMarkers().clear();
        mapPlaybackController.getMapPlaybackDataHolder().getPoints().clear();
        for (int i = 0; i < locations.size(); i++) {
            LatLng latLng = locations.get(i);
            final double latitude = latLng.latitude;
            final double longitude = latLng.longitude;

            //work for address and datetime update TODO Uncomment this
//            final String datetime = locDetailedObject.getString("datetime");
//            String address = locDetailedObject.getString("address");

//            if (!address.equals("")) {
//                String[] addressArray = address.split(",");
//                String streetAddress = addressArray[0] + ", " + addressArray[1];
//                addresses.add(streetAddress);
//            } else {
//                addresses.add("");

//                    if (i!=0) {
//                        JSONObject prevLocDetailedObject = mJSONLocArray.getJSONObject(i - 1);
//                        String prevAddress = prevLocDetailedObject.getString("address");
//                        String[] addressArray = prevAddress.split(",");
//                        String streetAddress = addressArray[0] + ", " + addressArray[1];
//                        addresses.add(streetAddress);
//                    }
//            }
            mapPlaybackController.addMarkerToMap(new LatLng(latitude, longitude), googleMap);
            mapPlaybackController.getMapPlaybackDataHolder().getPoints().add(new LatLng(latitude, longitude));
//            dates.add(datetime);
        }
        mapPlaybackController.startAnimation();
    }

    private void saveFromToLocationOnDataHolder(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude, MapPlaybackController mapPlaybackController) {
        MapPlaybackDataHolder mapPlaybackDataHolder = mapPlaybackController.getMapPlaybackDataHolder();
        mapPlaybackDataHolder.setFromLatitude(fromLatitude);
        mapPlaybackDataHolder.setFromLongitude(fromLongitude);
        mapPlaybackDataHolder.setToLatitude(toLatitude);
        mapPlaybackDataHolder.setToLongitude(toLongitude);
    }

    public void playPauseButtonOnClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPlay:
                if (MapPlaybackController.getInstance() != null) {
                    MapPlaybackController.getInstance().onClickPlay();
                } else {
                    Toast.makeText(this, "Please wait for Map to load!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonPause:
                if (MapPlaybackController.getInstance() != null) {
                    MapPlaybackController.getInstance().onClickPause();
                } else {
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

    public static boolean isForeground() {
        return isActivityInForeground;
    }
}
