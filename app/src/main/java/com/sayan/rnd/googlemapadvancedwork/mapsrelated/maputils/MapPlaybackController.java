package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities.MapsAnimationPlaybackActivity;

import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.ANIMATION_PAUSE;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.ANIMATION_PLAY;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.ANIMATION_SEEKING;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.DELAY;
import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.PLAYBACK_MARKER_TITLE;

public class MapPlaybackController {
    private static MapPlaybackController instance;
    private MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity;
    private GoogleMap.CancelableCallback MyCancelableCallback;
    private MapPlaybackDataHolder mapPlaybackDataHolder;
    private MapPlaybackViewHolder mapPlaybackViewHolder;
    private double playPauseLatitude;
    private double playPauseLongitude;

    private MapPlaybackController(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity, GoogleMap mMap) {
        this.mapsAnimationPlaybackActivity = mapsAnimationPlaybackActivity;
        this.mapPlaybackViewHolder = new MapPlaybackViewHolder(mapsAnimationPlaybackActivity);
        this.mapPlaybackDataHolder = MapPlaybackDataHolder.getInstance();
        this.mapPlaybackDataHolder.setmMap(mMap);
        this.MyCancelableCallback = new MapAnimationCallback(mapPlaybackDataHolder, this);
        setSeekBarChangeListener();
    }

    public static MapPlaybackController getInstance() {
        if (instance == null) {
            throw new SetupException(
                    "MapPlaybackController not initialized properly: Please call getInstance" +
                            "(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity, GoogleMap mMap)" +
                            " first to initialize MapPlaybackController class");
        }
        return instance;
    }

    public static MapPlaybackController getInstance(MapsAnimationPlaybackActivity mapsAnimationPlaybackActivity, GoogleMap mMap) {
        if (instance == null) {
            instance = new MapPlaybackController(mapsAnimationPlaybackActivity, mMap);
        }
        return instance;
    }

    public MapPlaybackDataHolder getMapPlaybackDataHolder() {
        return mapPlaybackDataHolder;
    }

    public MapPlaybackViewHolder getMapPlaybackViewHolder() {
        return mapPlaybackViewHolder;
    }

    public void initializeSeekBar(int maxValue) {
        mapPlaybackViewHolder.getSeekBar().getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mapPlaybackViewHolder.getSeekBar().setMax(maxValue);
    }

    public void addMarkerToMap(LatLng latLng, GoogleMap googleMap) {
        Marker marker = mapPlaybackViewHolder.addNewSnippetMarkerToMap(latLng, googleMap);
        mapPlaybackDataHolder.getMarkers().add(marker);
    }

    public void animateMarker(GoogleMap googleMap, final Marker playbackMarker, final LatLng targetLatLng, final boolean hideMarker, final int delay) {
        if (!mapPlaybackDataHolder.isSeekBarTouching()) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            if (mapPlaybackDataHolder.getmMap() == null) {
                return;
            }
            Projection proj = mapPlaybackDataHolder.getmMap().getProjection();
            Point startPoint = proj.toScreenLocation(playbackMarker.getPosition());
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = delay;
            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * targetLatLng.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * targetLatLng.latitude + (1 - t)
                            * startLatLng.latitude;
                    playbackMarker.setPosition(new LatLng(lat, lng));
                    if (!mapPlaybackDataHolder.isSeekBarTouching()) {
                        mapPlaybackDataHolder.updatePolyLine(new LatLng(lat, lng));
                    } else {
                        Log.d("Seek", "run: " + mapPlaybackDataHolder.isSeekBarTouching());
                    }
                    if (t < 1.0) {
                        // Post again some ms later.
                        handler.postDelayed(this, (delay / 167));
                    } else {
                        if (hideMarker) {
                            playbackMarker.setVisible(false);
                        } else {
                            playbackMarker.setPosition(targetLatLng);
                            playbackMarker.setVisible(true);
//                        updatePolyLine(toPosition);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(toPosition.latitude, toPosition.longitude)));
                        }
                    }
                }
            });
        }
    }

    public void onClickPlay() {
        if (mapPlaybackDataHolder.getCurrentPoint() == mapPlaybackDataHolder.getMarkers().size() - 1) {
            mapPlaybackDataHolder.clearData();
            mapsAnimationPlaybackActivity.onMapReady(mapPlaybackDataHolder.getmMap());
        } else {
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
        mapPlaybackDataHolder.setAnimationState(ANIMATION_PAUSE);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        mapPlaybackDataHolder.getmMap().getUiSettings().setAllGesturesEnabled(true);
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
        mapPlaybackDataHolder.setAnimationState(ANIMATION_PLAY);
        mapPlaybackDataHolder.getmMap().getUiSettings().setAllGesturesEnabled(false);
        mapPlaybackDataHolder.getmMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(playPauseLatitude, playPauseLongitude)));
        playPauseLatitude = 0.0;
        playPauseLongitude = 0.0;
        mapPlaybackDataHolder.getmMap().animateCamera(
                CameraUpdateFactory.zoomTo(/*mMap.getCameraPosition().zoom*/14.5f + 0.5f),
                1500,
                MyCancelableCallback);
        mapsAnimationPlaybackActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void startAnimation() {
        mapPlaybackDataHolder.setPolyLine(getMapPlaybackViewHolder().initializePolyLine(mapPlaybackDataHolder.getmMap(), mapPlaybackDataHolder.getMarkers()));
        mapPlaybackDataHolder.getmMap().getUiSettings().setZoomControlsEnabled(false);
        mapPlaybackDataHolder.getmMap().getUiSettings().setAllGesturesEnabled(false);
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
        mapPlaybackDataHolder.getmMap().animateCamera(
                CameraUpdateFactory.zoomTo(mapPlaybackDataHolder.getmMap().getCameraPosition().zoom + 0.5f),
                DELAY,
                MyCancelableCallback);
        mapPlaybackDataHolder.setCurrentPoint(0 - 1);
        mapsAnimationPlaybackActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void stopAnimation() {
//        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (mapPlaybackDataHolder.getCurrentPoint() == mapPlaybackDataHolder.getMarkers().size() - 1) {
            mapPlaybackViewHolder.getButtonPlay().setVisibility(View.VISIBLE);
            mapPlaybackViewHolder.getButtonPause().setVisibility(View.GONE);
        }
        mapPlaybackDataHolder.getmMap().getUiSettings().setAllGesturesEnabled(true);
//        dateTimeTextView.setText("DATE: "+historyDate+"     TIME: FROM "+end+" TO "+start);
        mapsAnimationPlaybackActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //seekbar touch skip playback
    private void setSeekBarChangeListener() {
        mapPlaybackViewHolder.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Seek", "onProgressChanged: " + progress + "fromUser: " + fromUser);
                if (fromUser) {
                    onClickPause();
                    mapPlaybackDataHolder.setCurrentPoint(progress);
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mapPlaybackDataHolder.getmMap().addPolyline(new PolylineOptions());
                    clearMap();
                    mapPlaybackViewHolder.drawMarker(
                            mapPlaybackDataHolder.getFromLatitude(),
                            mapPlaybackDataHolder.getFromLongitude(),
                            "From",
                            mapPlaybackDataHolder.getmMap(),
                            "https://interactive-examples.mdn.mozilla.net/media/examples/grapefruit-slice-332-332.jpg"
                    );
                    mapPlaybackDataHolder.getmMap().getUiSettings().setZoomControlsEnabled(false);
                    mapPlaybackDataHolder.getmMap().getUiSettings().setAllGesturesEnabled(true);
                    LatLng seekBarLatLong = mapPlaybackDataHolder.getPoints().get(progress);
                    PolylineOptions seekPolylineOptions = new PolylineOptions();
                    for (int i = 0; i <= progress; i++) {
                        seekPolylineOptions.add(mapPlaybackDataHolder.getPoints().get(i));
                    }
                    seekPolylineOptions.color(Color.BLUE);
                    seekPolylineOptions.geodesic(false);
                    seekPolylineOptions.width(6f);

                    mapPlaybackDataHolder.setPolyLine(mapPlaybackDataHolder.getmMap().addPolyline(seekPolylineOptions));

                    Marker marker = mapPlaybackViewHolder.drawMarker(
                            seekBarLatLong.latitude,
                            seekBarLatLong.longitude,
                            PLAYBACK_MARKER_TITLE,
                            mapPlaybackDataHolder.getmMap(),
                            false,
                            true
                    );
                    mapPlaybackDataHolder.setPlaybackMarker(marker);

                    //old replacement code
//
//                    mapPlaybackDataHolder.setPlaybackMarker(mMap.addMarker(
//                            new MarkerOptions()
//                                    .position(seekBarLatLong)
//                                    .title(PLAYBACK_MARKER_TITLE)
//                    ));
//                    mapPlaybackDataHolder.getPlaybackMarker()
//                            .setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(marker)));
//
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(seekBarLatLong.latitude, seekBarLatLong.longitude)));
//                    mMap.animateCamera(
//                            CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom + 0.5f),
//                            1,
//                            MySeekbarCallback);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mapPlaybackDataHolder.getAnimationState() == ANIMATION_PAUSE) {
                    mapPlaybackDataHolder.clearData();
                } else {
                    onClickPause();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mapPlaybackDataHolder.setAnimationState(ANIMATION_SEEKING);
                        }
                    }, DELAY);
                }
                mapPlaybackDataHolder.setSeekBarTouching(true);
                Log.d("Seek", "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Seek", "onStopTrackingTouch: ");
                playPauseLatitude = mapPlaybackDataHolder.getPoints().get(mapPlaybackDataHolder.getCurrentPoint()).latitude;
                playPauseLongitude = mapPlaybackDataHolder.getPoints().get(mapPlaybackDataHolder.getCurrentPoint()).longitude;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapPlaybackDataHolder.setSeekBarTouching(false);
                        if (mapPlaybackDataHolder.getAnimationState() == ANIMATION_SEEKING) {
                            onClickPlay();
                        }
                    }
                }, DELAY);
            }
        });
    }

    public void onBackPressed() {
        mapsAnimationPlaybackActivity.onBackPressed();
    }

    public void notifyDataSetChanged() {
        String currentDateTime = "";
        String currentAddress = "";
        if (mapPlaybackDataHolder.getDates() != null) {
            if (!mapPlaybackDataHolder.getDates().isEmpty()) {
                currentDateTime = mapPlaybackDataHolder.getDates().get(mapPlaybackDataHolder.getCurrentPoint());
            }
        }
        if (mapPlaybackDataHolder.getAddresses() != null) {
            if (!mapPlaybackDataHolder.getAddresses().isEmpty()) {
                currentAddress = mapPlaybackDataHolder.getAddresses().get(mapPlaybackDataHolder.getCurrentPoint());
            }
        }
        mapPlaybackViewHolder.notifyDataSetChanged(
                currentDateTime,
                currentAddress,
                mapPlaybackDataHolder.getCurrentPoint()
        );
    }

    public void clearMap() {
        mapPlaybackDataHolder.clearData();
    }
}
