package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.markerclusteringrelated;

import com.google.android.gms.maps.GoogleMap;

public class MarkerClusteringDataHolder {

    private static final MarkerClusteringDataHolder ourInstance = new MarkerClusteringDataHolder();
    private GoogleMap googleMap;

    private MarkerClusteringDataHolder() {
    }

    public static MarkerClusteringDataHolder getInstance() {
        return ourInstance;
    }

    public static MarkerClusteringDataHolder getInstance(GoogleMap googleMap) {
        ourInstance.googleMap = googleMap;
        return ourInstance;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
