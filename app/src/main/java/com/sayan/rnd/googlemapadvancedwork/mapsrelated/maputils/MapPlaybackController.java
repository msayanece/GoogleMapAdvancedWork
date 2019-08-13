package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.WindowManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities.MapsAnimationPlaybackActivity;

public class MapPlaybackController {
    private static MapPlaybackController instance;
    private int animationState = MapPlaybackConstants.ANIMATION_DEFAULT;
    private MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity;
    private GoogleMap.CancelableCallback MyCancelableCallback;
    private MapPlaybackDataHolder mapPlaybackDataHolder;
    private MapPlaybackViewHolder mapPlaybackViewHolder;
    private GoogleMap mMap;
    private double playPauseLatitude;
    private double playPauseLongitude;

    private MapPlaybackController(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity, GoogleMap mMap){
        this.mapsAnimationPlaybackActivity = mapsAnimationPlaybackActivity;
        this.mapPlaybackDataHolder = MapPlaybackDataHolder.getInstance();
        this.MyCancelableCallback = new MapAnimationCallback(mapPlaybackDataHolder, this);
        this.mMap = mMap;
    }

    public static MapPlaybackController getInstance(){
        if (instance == null){
            throw new SetupException(
                    "MapPlaybackController not initialized properly: Please call getInstance" +
                            "(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity, GoogleMap mMap)" +
                            " first to initialize MapPlaybackController class");
        }
        return instance;
    }

    public static MapPlaybackController getInstance(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity, GoogleMap mMap){
        if (instance == null){
            instance = new MapPlaybackController(mapsAnimationPlaybackActivity, mMap);
        }
        return instance;
    }

    public void setPlaybackMarker(Marker marker){
        mapPlaybackDataHolder.setPlaybackMarker(marker);
    }

    public void onClickPlay() {
        if (mapPlaybackDataHolder.getCurrentPoint() == mapPlaybackDataHolder.getMarkers().size()-1){
//            mapsAnimationPlaybackActivity.onMapReady(mMap);
        }else {
            if (playPauseLatitude != 0.0 && playPauseLongitude != 0.0) {
                playAnimation();
            }
        }
        mapPlaybackViewHolder.changePlayPauseButtonToPause();
    }

    public void onClickPause() {
        playPauseLatitude = mapPlaybackDataHolder.getMarkers().get(mapPlaybackDataHolder.getCurrentPoint()).getPosition().latitude;
        playPauseLongitude = mapPlaybackDataHolder.getMarkers().get(mapPlaybackDataHolder.getCurrentPoint()).getPosition().longitude;
        pauseAnimation();
        mapPlaybackViewHolder.changePlayPauseButtonToPlay();
    }

    private void pauseAnimation() {
        animationState = MapPlaybackConstants.ANIMATION_PAUSE;
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = mMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            GroupHistoryTrackingAnimation.this, R.raw.gpsinhandmapstylegreen));
//
//            if (!success) {
//                Log.e("sayan", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("sayan", "Can't find style. Error: ", e);
//        }
        mapsAnimationPlaybackActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void playAnimation() {
        animationState = MapPlaybackConstants.ANIMATION_PLAY;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(playPauseLatitude, playPauseLongitude)));
        playPauseLatitude = 0.0;
        playPauseLongitude = 0.0;
        mMap.animateCamera(
                CameraUpdateFactory.zoomTo(/*mMap.getCameraPosition().zoom*/14.5f + 0.5f),
                1500,
                MyCancelableCallback);
        mapsAnimationPlaybackActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void stopAnimation() {
//        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (mapPlaybackDataHolder.getCurrentPoint() == mapPlaybackDataHolder.getMarkers().size()-1) {
            mapPlaybackViewHolder.getButtonPlay().setVisibility(View.VISIBLE);
            mapPlaybackViewHolder.getButtonPause().setVisibility(View.GONE);
        }
        mMap.getUiSettings().setAllGesturesEnabled(true);
//        dateTimeTextView.setText("DATE: "+historyDate+"     TIME: FROM "+end+" TO "+start);
        mapsAnimationPlaybackActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void onBackPressed() {
        mapsAnimationPlaybackActivity.onBackPressed();
    }

    public void notifyDataSetChanged(){
        String currentDateTime = mapPlaybackDataHolder.getDates().get(mapPlaybackDataHolder.getCurrentPoint());
        String currentAddress = mapPlaybackDataHolder.getAddresses().get(mapPlaybackDataHolder.getCurrentPoint());
        mapsAnimationPlaybackActivity.notifyDataSetChanged(
                currentDateTime,
                currentAddress,
                mapPlaybackDataHolder.getCurrentPoint()
        );
    }
}
