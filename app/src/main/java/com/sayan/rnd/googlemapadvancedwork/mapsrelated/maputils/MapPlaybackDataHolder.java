package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

import static com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.MapPlaybackConstants.ANIMATION_DEFAULT;

public class MapPlaybackDataHolder {

    private static MapPlaybackDataHolder instance;

    private int currentPoint;

    //properties
    private ArrayList<LatLng> points;
    private ArrayList<String> dates;
    private ArrayList<String> addresses;
    private ArrayList<Marker> markers;
    private boolean isSeekBarTouching;
    private Polyline polyLine;
    private GoogleMap mMap;
    private Marker playbackMarker;
    private int animationState = ANIMATION_DEFAULT;

    private double fromLatitude;
    private double fromLongitude;
    private double toLatitude;
    private double toLongitude;

    private MapPlaybackDataHolder() {
        initializeProperties();
    }

    private MapPlaybackDataHolder(int currentPoint, ArrayList<Marker> markers) {
        this.currentPoint = currentPoint;
        this.markers = markers;
        initializeProperties();
    }

    private void initializeProperties() {
        points = new ArrayList<>();
        dates = new ArrayList<>();
        addresses = new ArrayList<>();
        markers = new ArrayList<>();
    }

    public static MapPlaybackDataHolder getInstance(){
        if (instance == null){
            instance = new MapPlaybackDataHolder();
        }
        return instance;
    }

    public static MapPlaybackDataHolder getInstance(int currentPt, ArrayList<Marker> markers){
        if (instance == null){
            instance = new MapPlaybackDataHolder(currentPt, markers);
        }
        return instance;
    }

    //region getters
    public int getAnimationState() {
        return animationState;
    }

    public Marker getPlaybackMarker() {
        return playbackMarker;
    }

    public boolean isSeekBarTouching() {
        return isSeekBarTouching;
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public GoogleMap getGoogleMap() {
        return mMap;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public Polyline getPolyLine() {
        return polyLine;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public double getFromLatitude() {
        return fromLatitude;
    }

    public double getFromLongitude() {
        return fromLongitude;
    }

    public double getToLatitude() {
        return toLatitude;
    }

    public double getToLongitude() {
        return toLongitude;
    }

    //endregion

    //region setters

    public void setAnimationState(int animationState) {
        this.animationState = animationState;
    }

    public void setPlaybackMarker(Marker playbackMarker) {
        this.playbackMarker = playbackMarker;
    }

    public void setSeekBarTouching(boolean seekBarTouching) {
        isSeekBarTouching = seekBarTouching;
    }

    public void setPoints(ArrayList<LatLng> points) {
        this.points = points;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }

    public void setAddresses(ArrayList<String> addresses) {
        this.addresses = addresses;
    }

    public void setMarkers(ArrayList<Marker> markers) {
        this.markers = markers;
    }

    public void setCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
    }

    public static void setInstance(MapPlaybackDataHolder instance) {
        MapPlaybackDataHolder.instance = instance;
    }

    public void setPolyLine(Polyline polyLine) {
        this.polyLine = polyLine;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public void setFromLatitude(double fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public void setFromLongitude(double fromLongitude) {
        this.fromLongitude = fromLongitude;
    }

    public void setToLatitude(double toLatitude) {
        this.toLatitude = toLatitude;
    }

    public void setToLongitude(double toLongitude) {
        this.toLongitude = toLongitude;
    }

    //endregion

    /**
     * Add the marker to the polyline.
     */
    public void updatePolyLine(LatLng latLng) {
        List<LatLng> points = polyLine.getPoints();
        points.add(latLng);
        polyLine.setPoints(points);
    }

    //region property util methods
    public int incrementCurrentPoint(){
        return ++currentPoint;
    }
    //endregion
}
