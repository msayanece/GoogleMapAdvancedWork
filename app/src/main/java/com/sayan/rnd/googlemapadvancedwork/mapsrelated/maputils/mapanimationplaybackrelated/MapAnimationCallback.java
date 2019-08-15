package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated;

import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities.MapsAnimationPlaybackActivity;

import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated.MapPlaybackConstants.ANIMATION_DEFAULT;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated.MapPlaybackConstants.ANIMATION_PAUSE;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated.MapPlaybackConstants.ANIMATION_PLAY;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated.MapPlaybackConstants.DELAY;

public class MapAnimationCallback implements GoogleMap.CancelableCallback{

    private MapPlaybackDataHolder mapPlaybackDataHolder;
    private MapPlaybackController mapPlaybackController;

    private GoogleMap.CancelableCallback mFinalCancelableCallback = new GoogleMap.CancelableCallback() {

        @Override
        public void onFinish() {
            mapPlaybackController.stopAnimation();
//            GoogleMapUtis.fixZoomForMarkers(mMap,markers);
        }

        @Override
        public void onCancel() {

        }
    };

    public MapAnimationCallback(MapPlaybackDataHolder mapPlaybackDataHolder, MapPlaybackController mapPlaybackController) {
        this.mapPlaybackDataHolder = mapPlaybackDataHolder;
        this.mapPlaybackController = mapPlaybackController;
    }

    @Override
    public void onCancel() {
        try {
            mapPlaybackController.onClickPause();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            mapPlaybackController.onBackPressed();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MapsAnimationPlaybackActivity.isForeground()) {
                    mapPlaybackController.onClickPlay();
                }else {
                    mapPlaybackController.onBackPressed();
                }
            }
        },50);
    }

    @Override
    public void onFinish() {
        if(mapPlaybackDataHolder.incrementCurrentPoint() < mapPlaybackDataHolder.getMarkers().size()){
            try {
                float targetBearing = MapAnimationUtil.bearingBetweenLatLngs(
                        mapPlaybackDataHolder.getGoogleMap().getCameraPosition().target,
                        mapPlaybackDataHolder.getMarkers().get(mapPlaybackDataHolder.getCurrentPoint()).getPosition());
                LatLng targetLatLng = mapPlaybackDataHolder.getMarkers().get(mapPlaybackDataHolder.getCurrentPoint()).getPosition();

                System.out.println(" ------- " + mapPlaybackDataHolder.getCurrentPoint() + " - " + mapPlaybackDataHolder.getMarkers().size() + " - " + targetBearing + " - " + targetLatLng);

                CameraPosition cameraPosition =
                        new CameraPosition.Builder()
                                .target(targetLatLng)
                                .tilt(mapPlaybackDataHolder.getCurrentPoint() < mapPlaybackDataHolder.getMarkers().size() - 1 ? 10 : 0)
                                .bearing(targetBearing)
                                .zoom(mapPlaybackDataHolder.getGoogleMap().getCameraPosition().zoom)
                                .build();
                mapPlaybackController.notifyDataSetChanged();

                switch (mapPlaybackDataHolder.getAnimationState()){
                    case ANIMATION_DEFAULT:
                        if (!mapPlaybackDataHolder.isSeekBarTouching()) {
                            mapPlaybackDataHolder.getGoogleMap().animateCamera(
                                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                                    DELAY,
                                    mapPlaybackDataHolder.getCurrentPoint() ==
                                            mapPlaybackDataHolder.getMarkers().size() - 1 ?
                                            mFinalCancelableCallback :
                                            this
                            );
//						googleMap.moveCamera(
//								CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mapPlaybackController.animateMarker(mapPlaybackDataHolder.getGoogleMap(), mapPlaybackDataHolder.getPlaybackMarker(), targetLatLng, false, DELAY);
//                                markers.get(currentPt).showInfoWindow();
                        }
                        break;

                    case ANIMATION_PAUSE:
                        if (!mapPlaybackDataHolder.isSeekBarTouching()) {
//                                mapPlaybackDataHolder.getGoogleMap().animateCamera(
//                                        CameraUpdateFactory.newCameraPosition(cameraPosition),
//                                        delay, FinalCancelableCallback);
////						googleMap.moveCamera(
////								CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                                setMarker(playbackMarker, targetLatLng);
//                                markers.get(currentPt).showInfoWindow();
                        }
                        break;

                    case ANIMATION_PLAY:
                        if (!mapPlaybackDataHolder.isSeekBarTouching()) {
                            mapPlaybackDataHolder.getGoogleMap().animateCamera(
                                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                                    DELAY,
                                    mapPlaybackDataHolder.getCurrentPoint() ==
                                            mapPlaybackDataHolder.getMarkers().size() - 1 ?
                                            mFinalCancelableCallback :
                                            this
                            );

//						googleMap.moveCamera(
//								CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mapPlaybackController.animateMarker(mapPlaybackDataHolder.getGoogleMap(), mapPlaybackDataHolder.getPlaybackMarker(), targetLatLng, false, DELAY);
//                                markers.get(currentPt).showInfoWindow();
                        }
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
