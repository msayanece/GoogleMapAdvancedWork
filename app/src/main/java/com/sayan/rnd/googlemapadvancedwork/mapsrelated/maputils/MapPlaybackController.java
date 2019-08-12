package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import android.view.WindowManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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
        this.MyCancelableCallback = new MapAnimationCallback(mapsAnimationPlaybackActivity);
        this.mapPlaybackDataHolder = MapPlaybackDataHolder.getInstance();
        this.mMap = mMap;
    }

    public static MapPlaybackController getInstance(){
        return instance;
    }

    public static MapPlaybackController getInstance(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity, GoogleMap mMap){
        if (instance == null){
            instance = new MapPlaybackController(mapsAnimationPlaybackActivity, mMap);
        }
        return instance;
    }

    public void onClickPlay() {
        if (mapPlaybackDataHolder.getCurrentPt() == mapPlaybackDataHolder.getMarkers().size()-1){
//            mapsAnimationPlaybackActivity.onMapReady(mMap);
        }else {
            if (playPauseLatitude != 0.0 && playPauseLongitude != 0.0) {
                playAnimation();
            }
        }
        mapPlaybackViewHolder.changePlayPauseButtonToPause();
    }

    public void onClickPause() {
        playPauseLatitude = mapPlaybackDataHolder.getMarkers().get(mapPlaybackDataHolder.getCurrentPt()).getPosition().latitude;
        playPauseLongitude = mapPlaybackDataHolder.getMarkers().get(mapPlaybackDataHolder.getCurrentPt()).getPosition().longitude;
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
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = mMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            GroupHistoryTrackingAnimation.this, R.raw.gpsinhandmapstylegreenanimation));
//            if (!success) {
//                Log.e("sayan", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("sayan", "Can't find style. Error: ", e);
//        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(playPauseLatitude, playPauseLongitude)));
        playPauseLatitude = 0.0;
        playPauseLongitude = 0.0;
        mMap.animateCamera(
                CameraUpdateFactory.zoomTo(/*mMap.getCameraPosition().zoom*/14.5f + 0.5f),
                1500,
                MyCancelableCallback);
        mapsAnimationPlaybackActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
