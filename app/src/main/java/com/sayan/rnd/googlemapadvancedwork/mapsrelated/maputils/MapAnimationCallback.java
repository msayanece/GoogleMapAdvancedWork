package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities.MapsAnimationPlaybackActivity;

public class MapAnimationCallback implements GoogleMap.CancelableCallback{

    private MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity;

    public MapAnimationCallback(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity) {
        this.mapsAnimationPlaybackActivity = mapsAnimationPlaybackActivity;
    }

    @Override
    public void onFinish() {
//        try {
//            onClickPause();
//        }catch (IndexOutOfBoundsException e){
//            e.printStackTrace();
//            mapsAnimationPlaybackActivity.onBackPressed();
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (MapsAnimationPlaybackActivity.isForeground()) {
//                    onClickPlay();
//                }else {
//                    mapsAnimationPlaybackActivity.onBackPressed();
//                }
//            }
//        },50);
    }

    @Override
    public void onCancel() {
//        if(++currentPt < markers.size()){
//            try {
//                float targetBearing = bearingBetweenLatLngs(mMap.getCameraPosition().target, markers.get(currentPt).getPosition());
//                LatLng targetLatLng = markers.get(currentPt).getPosition();
//
//                System.out.println(" ------- " + currentPt + " - " + markers.size() + " - " + targetBearing + " - " + targetLatLng);
//
//                CameraPosition cameraPosition =
//                        new CameraPosition.Builder()
//                                .target(targetLatLng)
//                                .tilt(currentPt < markers.size() - 1 ? 10 : 0)
//                                .bearing(targetBearing)
//                                .zoom(mMap.getCameraPosition().zoom)
//                                .build();
//                dateTimeTextView.setText(dates.get(currentPt));
//                if (!addresses.get(currentPt).equals("")) {
//                    addressTextView.setText(addresses.get(currentPt));
//                }
//                seekBar.setProgress(currentPt);
//                seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//                switch (animationState){
//                    case ANIMATION_DEFAULT:
//                        if (!isSeekBarTouching) {
//                            mMap.animateCamera(
//                                    CameraUpdateFactory.newCameraPosition(cameraPosition),
//                                    delay,
//                                    currentPt == markers.size() - 1 ? FinalCancelableCallback : MyCancelableCallback);
////						googleMap.moveCamera(
////								CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                            animateMarker(playbackMarker, targetLatLng, false);
////                                markers.get(currentPt).showInfoWindow();
//                        }
//                        break;
//
//                    case ANIMATION_PAUSE:
//                        if (!isSeekBarTouching) {
////                                mMap.animateCamera(
////                                        CameraUpdateFactory.newCameraPosition(cameraPosition),
////                                        delay, FinalCancelableCallback);
//////						googleMap.moveCamera(
//////								CameraUpdateFactory.newCameraPosition(cameraPosition));
////
////                                setMarker(playbackMarker, targetLatLng);
////                                markers.get(currentPt).showInfoWindow();
//                        }
//                        break;
//
//                    case ANIMATION_PLAY:
//                        if (!isSeekBarTouching) {
//                            mMap.animateCamera(
//                                    CameraUpdateFactory.newCameraPosition(cameraPosition),
//                                    delay,
//                                    currentPt == markers.size() - 1 ? FinalCancelableCallback : MyCancelableCallback);
//
////						googleMap.moveCamera(
////								CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                            animateMarker(playbackMarker, targetLatLng, false);
////                                markers.get(currentPt).showInfoWindow();
//                        }
//                        break;
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }
}
