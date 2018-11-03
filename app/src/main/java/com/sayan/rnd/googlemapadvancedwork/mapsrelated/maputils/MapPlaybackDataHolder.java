package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MapPlaybackDataHolder {

    private static MapPlaybackDataHolder instance;

    private int currentPt;
    private ArrayList<Marker> markers;
    private MapPlaybackViewHolder mapPlaybackViewHolder;

    private MapPlaybackDataHolder() {}

    private MapPlaybackDataHolder(int currentPt, ArrayList<Marker> markers, MapPlaybackViewHolder mapPlaybackViewHolder) {
        this.currentPt = currentPt;
        this.markers = markers;
        this.mapPlaybackViewHolder = mapPlaybackViewHolder;
    }

    public static MapPlaybackDataHolder getInstance(){
        if (instance == null){
            instance = new MapPlaybackDataHolder();
        }
        return instance;
    }

    public static MapPlaybackDataHolder getInstance(int currentPt, ArrayList<Marker> markers, MapPlaybackViewHolder mapPlaybackViewHolder){
        if (instance == null){
            instance = new MapPlaybackDataHolder(currentPt, markers, mapPlaybackViewHolder);
        }
        return instance;
    }

    public int getCurrentPt() {
        return currentPt;
    }

    public void setCurrentPt(int currentPt) {
        this.currentPt = currentPt;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<Marker> markers) {
        this.markers = markers;
    }

    public MapPlaybackViewHolder getMapPlaybackViewHolder() {
        return mapPlaybackViewHolder;
    }

    public void setMapPlaybackViewHolder(MapPlaybackViewHolder mapPlaybackViewHolder) {
        this.mapPlaybackViewHolder = mapPlaybackViewHolder;
    }
}
